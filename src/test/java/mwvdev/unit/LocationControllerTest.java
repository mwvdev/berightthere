package mwvdev.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import mwvdev.entity.LocationEntity;
import mwvdev.entity.TripEntity;
import mwvdev.model.TripIdentifier;
import mwvdev.repository.TripRepository;
import mwvdev.service.UuidGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = LocationController.class, secure = false)
public class LocationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private UuidGenerator uuidGenerator;

    @Test
    public void canCheckin() throws Exception {
        String tripIdentifier = "0686be0a-cbd8-4f28-b972-17f010acddc6";
        given(uuidGenerator.generate()).willReturn(UUID.fromString(tripIdentifier));

        String expectedContent = objectMapper.writeValueAsString(new TripIdentifier(tripIdentifier));

        this.mvc.perform(get("/api/trip/checkin").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(content().string(expectedContent));
    }

    @Test
    public void canAddLocation() throws Exception {
        String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(
                createTripEntity(tripIdentifier));

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, 55.6782377, 12.5594759).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void canAddLocationWithOptionalParameters() throws Exception {
        String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(
                createTripEntity(tripIdentifier));

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}?accuracy={accuracy}",
                tripIdentifier, 55.6782377, 12.5594759, 15.23).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addLocation_ThrowsWhenInvalidLocations() throws Exception {
        String tripIdentifier = "9b50d73c-c503-4562-8852-c61c3defe0e0";
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(
                createTripEntity(tripIdentifier));

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, -120, 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, 0, 190).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addLocation_ThrowsWhenUnknownTrip() throws Exception {
        String tripIdentifier = "2238af77-343c-45c9-b2b2-2c0bb9b2ba96";
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(null);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/addLocation/{latitude}/{longitude}",
                tripIdentifier, 0, 0).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void canGetLocations() throws Exception {
        String tripIdentifier = "acc79c61-37e0-44cf-86be-8fe0a52371c5";
        TripEntity tripEntity = createTripEntity(tripIdentifier);
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(
                tripEntity);

        String expectedContent = objectMapper.writeValueAsString(tripEntity.getLocations());

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().string(expectedContent));
    }

    @Test
    public void getLocations_ThrowsWhenUnknownTrip() throws Exception {
        String tripIdentifier = "4458cbb0-aed8-4732-bc4f-6641949a23fc";
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(null);

        this.mvc.perform(get("/api/trip/{tripIdentifier}/locations", tripIdentifier).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private TripEntity createTripEntity(String tripIdentifier) {
        TripEntity trip = new TripEntity(tripIdentifier);
        trip.setId(42L);
        trip.setLocationEntities(createLocationEntities(trip));
        return trip;
    }

    private List<LocationEntity> createLocationEntities(TripEntity trip) {
        return new ArrayList<>(Arrays.asList(new LocationEntity(trip.getId(), 55.6739062, 12.5556993, 7.5),
                new LocationEntity(trip.getId(), 55.6746322, 12.5585318, 5.2),
                new LocationEntity(trip.getId(), 55.6764229, 12.5588751, 3.3)));
    }
    
}