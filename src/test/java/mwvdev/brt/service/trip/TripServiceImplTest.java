package mwvdev.brt.service.trip;

import mwvdev.brt.TripTestHelper;
import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.repository.TripRepository;
import mwvdev.brt.service.UuidGenerator;
import mwvdev.brt.service.mapper.LocationMapper;
import mwvdev.brt.service.mapper.TripMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TripServiceImplTest {

    private TripService tripService;

    @MockBean
    private TripRepository tripRepository;

    @MockBean
    private UuidGenerator uuidGenerator;

    @MockBean
    private TripMapper tripMapper;

    @MockBean
    private LocationMapper locationMapper;

    private static final String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";
    private static final double latitude = 55.6912296;
    private static final double longitude = 12.5714064;
    private static final double accuracy = 0.92;

    @Before
    public void setUp() {
        tripService = new TripServiceImpl(tripRepository, tripMapper, locationMapper, uuidGenerator);
    }

    @Test
    public void canCheckin() {
        when(uuidGenerator.generate()).thenReturn(UUID.fromString(tripIdentifier));

        String tripIdentifier = tripService.checkin();

        assertThat(tripIdentifier, is(tripIdentifier));
    }

    @Test
    public void canAddLocation() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(tripEntity);
        Location expectedLocation = mock(Location.class);
        when(locationMapper.toLocation(any())).thenReturn(expectedLocation);

        Location actualLocation = tripService.addLocation(tripIdentifier, latitude, longitude, accuracy);

        verify(tripRepository).save(tripEntity);
        ArgumentCaptor<LocationEntity> locationEntityCaptor = ArgumentCaptor.forClass(LocationEntity.class);
        verify(locationMapper).toLocation(locationEntityCaptor.capture());

        assertThat(actualLocation, is(expectedLocation));

        LocationEntity createdLocationEntity = locationEntityCaptor.getValue();
        assertThat(tripEntity.getLocations(), hasItem(createdLocationEntity));
        assertThat(createdLocationEntity.getLatitude(), is(latitude));
        assertThat(createdLocationEntity.getLongitude(), is(longitude));
        assertThat(createdLocationEntity.getAccuracy(), is(accuracy));
    }

    @Test(expected = UnknownTripException.class)
    public void addLocation_WhenMissingTrip_Throws() {
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(null);

        tripService.addLocation(tripIdentifier, latitude, longitude, accuracy);
    }

    @Test
    public void canGetTrip() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(tripEntity);
        Trip expectedTrip = TripTestHelper.createTrip(tripIdentifier);
        when(tripMapper.toTrip(tripEntity)).thenReturn(expectedTrip);

        Trip actualTrip = tripService.getTrip(tripIdentifier);

        assertThat(actualTrip, is(expectedTrip));
    }

    @Test(expected = UnknownTripException.class)
    public void getTrip_WhenMissingTrip_Throws() {
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(null);

        tripService.getTrip(tripIdentifier);
    }
}
