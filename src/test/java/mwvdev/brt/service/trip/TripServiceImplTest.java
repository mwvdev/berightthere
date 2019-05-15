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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TripServiceImpl.class)
class TripServiceImplTest {

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

    @BeforeEach
    void setUp() {
        tripService = new TripServiceImpl(tripRepository, tripMapper, locationMapper, uuidGenerator);
    }

    @Test
    void canCheckin() {
        when(uuidGenerator.generate()).thenReturn(UUID.fromString(tripIdentifier));

        String tripIdentifier = tripService.checkin();

        assertThat(tripIdentifier, is(tripIdentifier));
    }

    @Test
    void canAddLocation() {
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

    @Test()
    void addLocation_WhenMissingTrip_Throws() {
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(null);

        assertThrows(UnknownTripException.class, () ->
                tripService.addLocation(tripIdentifier, latitude, longitude, accuracy));
    }

    @Test
    void canGetTrip() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(tripEntity);
        Trip expectedTrip = TripTestHelper.createTrip(tripIdentifier);
        when(tripMapper.toTrip(tripEntity)).thenReturn(expectedTrip);

        Trip actualTrip = tripService.getTrip(tripIdentifier);

        assertThat(actualTrip, is(expectedTrip));
    }

    @Test()
    void getTrip_WhenMissingTrip_Throws() {
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(null);

        assertThrows(UnknownTripException.class, () -> tripService.getTrip(tripIdentifier));
    }
    
}
