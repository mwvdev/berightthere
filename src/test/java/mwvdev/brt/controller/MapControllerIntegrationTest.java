package mwvdev.brt.controller;

import mwvdev.brt.TripTestHelper;
import mwvdev.brt.model.Trip;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(MapController.class)
class MapControllerIntegrationTest {

    @MockBean
    private TripService tripService;

    @Autowired
    private MockMvc mvc;

    private static final String tripIdentifier = "ab7d8df0-e952-4956-8c38-0351b90fd045";

    @Test
    void canViewTrip() throws Exception {
        when(tripService.getTrip(tripIdentifier)).thenReturn(TripTestHelper.createTrip(tripIdentifier));

        this.mvc.perform(get("/trip/{tripIdentifier}", tripIdentifier)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("map"));
    }

    @Test
    void viewTrip_WhenUnknownTrip_RendersUnknownView() throws Exception {
        when(tripService.getTrip(tripIdentifier)).thenThrow(UnknownTripException.class);

        this.mvc.perform(get("/trip/{tripIdentifier}", tripIdentifier)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("unknown"));
    }

    @Test
    void viewTrip_WhenUnknownTrip_RendersWaitingView() throws Exception {
        Trip trip = TripTestHelper.createTrip(tripIdentifier);
        trip.getLocations().clear();
        when(tripService.getTrip(tripIdentifier)).thenReturn(trip);

        this.mvc.perform(get("/trip/{tripIdentifier}", tripIdentifier)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("waiting"));
    }

}
