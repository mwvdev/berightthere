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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private static final Location location = TripTestHelper.createReferenceLocation();

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

        LocationEntity locationEntity = mock(LocationEntity.class);
        when(locationMapper.toEntity(tripEntity.getId(), location)).thenReturn(locationEntity);

        tripService.addLocation(tripIdentifier, location);

        verify(tripRepository).save(tripEntity);

        List<LocationEntity> locations = tripEntity.getLocations();
        LocationEntity lastLocationEntity = locations.get(locations.size() - 1);

        assertThat(lastLocationEntity, is(locationEntity));
    }

    @Test()
    void addLocation_WhenMissingTrip_Throws() {
        when(tripRepository.findByTripIdentifier(tripIdentifier)).thenReturn(null);

        assertThrows(UnknownTripException.class, () ->
                tripService.addLocation(tripIdentifier, location));
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
