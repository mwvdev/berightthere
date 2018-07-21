package mwvdev.brt.service;

import mwvdev.brt.TripTestHelper;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.repository.TripRepository;
import mwvdev.brt.service.location.TripService;
import mwvdev.brt.service.location.TripServiceImpl;
import mwvdev.brt.service.location.UnknownTripException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class TripServiceImplTest {

    private TripService tripService;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private UuidGenerator uuidGenerator;

    private static final String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
    private static final double latitude = 55.6912296;
    private static final double longitude = 12.5714064;
    private static final double accuracy = 0.92;

    @Before
    public void setUp() {
        tripService = new TripServiceImpl(tripRepository, uuidGenerator);
    }

    @Test
    public void canCheckin() {
        given(uuidGenerator.generate()).willReturn(UUID.fromString(tripIdentifier));

        String tripIdentifier = tripService.checkin();

        assertThat(tripIdentifier, is(tripIdentifier));
    }

    @Test
    public void canAddLocation() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(tripEntity);

        Location location = tripService.addLocation(tripIdentifier, latitude, longitude, accuracy);

        verify(tripRepository).save(tripEntity);
        assertThat(location.getLatitude(), is(latitude));
        assertThat(location.getLongitude(), is(longitude));
        assertThat(location.getAccuracy(), is(accuracy));
        assertThat(tripEntity.getLocations(), hasItem(location));
    }

    @Test(expected = UnknownTripException.class)
    public void addLocation_WhenMissingTrip_Throws() {
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(null);

        tripService.addLocation(tripIdentifier, latitude, longitude, accuracy);
    }

    @Test
    public void canGetLocations() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(tripEntity);

        List<Location> locations = tripService.getLocations(tripIdentifier);

        assertThat(locations, is(tripEntity.getLocations()));
    }

    @Test(expected = UnknownTripException.class)
    public void getLocations_WhenMissingTrip_Throws() {
        given(tripRepository.findByTripIdentifier(tripIdentifier)).willReturn(null);

        tripService.getLocations(tripIdentifier);
    }
}
