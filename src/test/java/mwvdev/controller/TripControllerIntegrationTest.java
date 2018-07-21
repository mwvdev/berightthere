package mwvdev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.TripTestHelper;
import mwvdev.entity.LocationEntity;
import mwvdev.entity.TripEntity;
import mwvdev.model.TripIdentifier;
import mwvdev.service.location.TripService;
import mwvdev.service.location.UnknownTripException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = TripController.class, secure = false)
public class TripControllerIntegrationTest {

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
    public void canCheckin() throws Exception {
        given(tripService.checkin()).willReturn(tripIdentifier);

        String expectedContent = objectMapper.writeValueAsString(new TripIdentifier(tripIdentifier));

        this.mvc.perform(get("/api/trip/checkin").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void canAddLocation() throws Exception {
        LocationEntity locationEntity = new LocationEntity(1, latitude, longitude, accuracy);
        given(tripService.addLocation(tripIdentifier, latitude, longitude, null)).willReturn(
                locationEntity);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, latitude, longitude).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(simpMessagingTemplate).convertAndSend("/topic/" + tripIdentifier, locationEntity);
    }

    @Test
    public void canAddLocationWithOptionalParameters() throws Exception {
        LocationEntity locationEntity = new LocationEntity(1, latitude, longitude, accuracy);
        given(tripService.addLocation(tripIdentifier, latitude, longitude, accuracy)).willReturn(
                locationEntity);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}?accuracy={accuracy}",
                tripIdentifier, latitude, longitude, accuracy).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(simpMessagingTemplate).convertAndSend("/topic/" + tripIdentifier, locationEntity);
    }

    @Test
    public void addLocation_WhenInvalidLocations_Throws() throws Exception {
        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, -120, 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, 0, 190).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addLocation_WhenUnknownTrip_Throws() throws Exception {
        given(tripService.addLocation(tripIdentifier, latitude, longitude, null)).willThrow(UnknownTripException.class);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, latitude, longitude).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void canGetLocations() throws Exception {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        given(tripService.getLocations(tripIdentifier)).willReturn(tripEntity.getLocations());

        String expectedContent = objectMapper.writeValueAsString(tripEntity.getLocations());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(expectedContent));
    }

    @Test
    public void getLocations_WhenUnknownTrip_ReturnsNotFound() throws Exception {
        given(tripService.getLocations(tripIdentifier)).willThrow(new UnknownTripException());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
}