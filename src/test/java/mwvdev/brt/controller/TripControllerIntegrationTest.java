package mwvdev.brt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.brt.TripTestHelper;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.model.TripIdentifier;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    private static final Location location = TripTestHelper.createReferenceLocation();

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
        this.mvc.perform(post("/api/trip/{tripIdentifier}/addLocation", tripIdentifier)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ArgumentCaptor<Location> locationArgumentCaptor = ArgumentCaptor.forClass(Location.class);
        verify(simpMessagingTemplate).convertAndSend(eq("/topic/" + tripIdentifier), locationArgumentCaptor.capture());

        Location actualLocation = locationArgumentCaptor.getValue();
        assertThat(actualLocation, Matchers.samePropertyValuesAs(location));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidLocations")
    void addLocation_WhenInvalidLocations_Throws(Location location) throws Exception {
        this.mvc.perform(post("/api/trip/{tripIdentifier}/addLocation", tripIdentifier)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private static Stream<Location> provideInvalidLocations() {
        return Stream.of(
                new Location(-100, 0, null),
                new Location(100, 0, null),
                new Location(0, -190, null),
                new Location(0, 190, null));
    }

    @Test
    void addLocation_WhenUnknownTrip_Throws() throws Exception {
        doThrow(UnknownTripException.class)
                .when(tripService)
                .addLocation(eq(tripIdentifier), any());

        this.mvc.perform(post("/api/trip/{tripIdentifier}/addLocation", tripIdentifier)
                .content(objectMapper.writeValueAsString(location))
                .contentType(MediaType.APPLICATION_JSON)
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