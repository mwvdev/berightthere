package mwvdev.brt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.brt.TripTestHelper;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.LocationImpl;
import mwvdev.brt.model.Trip;
import mwvdev.brt.model.TripIdentifier;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TripController.class)
class TripControllerIntegrationTest {

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private TripService tripService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
    private static final double latitude = 55.6782377;
    private static final double longitude = 12.5594759;
    private static final double accuracy = 15.23;

    @Test
    void canCheckin() throws Exception {
        when(tripService.checkin()).thenReturn(tripIdentifier);

        String expectedContent = objectMapper.writeValueAsString(new TripIdentifier(tripIdentifier));

        this.mvc.perform(get("/api/trip/checkin")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void canAddLocation() throws Exception {
        Location location = mock(Location.class);
        when(tripService.addLocation(tripIdentifier, latitude, longitude, null)).thenReturn(location);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, latitude, longitude)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(simpMessagingTemplate).convertAndSend("/topic/" + tripIdentifier, location);
    }

    @Test
    void canAddLocationWithOptionalParameters() throws Exception {
        Location location = mock(Location.class);
        when(tripService.addLocation(tripIdentifier, latitude, longitude, accuracy)).thenReturn(location);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}?accuracy={accuracy}",
                tripIdentifier, latitude, longitude, accuracy)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(simpMessagingTemplate).convertAndSend("/topic/" + tripIdentifier, location);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLocations")
    void addLocation_WhenInvalidLocations_Throws(Location location) throws Exception {
        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, location.getLatitude(), location.getLongitude())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Location> provideInvalidLocations() {
        return Stream.of(
                new LocationImpl(-100, 0, null),
                new LocationImpl(100, 0, null),
                new LocationImpl(0, -190, null),
                new LocationImpl(0, 190, null));
    }

    @Test
    void addLocation_WhenUnknownTrip_Throws() throws Exception {
        when(tripService.addLocation(tripIdentifier, latitude, longitude, null)).thenThrow(UnknownTripException.class);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, latitude, longitude)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void canGetLocations() throws Exception {
        Trip trip = TripTestHelper.createTrip(tripIdentifier);
        when(tripService.getTrip(tripIdentifier)).thenReturn(trip);

        String expectedContent = objectMapper.writeValueAsString(trip.getLocations());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    void getLocations_WhenUnknownTrip_ReturnsNotFound() throws Exception {
        when(tripService.getTrip(tripIdentifier)).thenThrow(new UnknownTripException());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
}