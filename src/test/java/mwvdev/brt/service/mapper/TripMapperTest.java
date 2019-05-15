package mwvdev.brt.service.mapper;

import mwvdev.brt.TripTestHelper;
import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class TripMapperTest {
    private TripMapper tripMapper;

    private static final String tripIdentifier = "0c98b95e-848f-4589-a7f9-dcc7dde95725";

    @BeforeEach
    void setUp() {
        LocationMapper locationMapper = new LocationMapperImpl();
        tripMapper = new TripMapperImpl(locationMapper);
    }

    @Test
    void canMapToTrip() {
        TripEntity tripEntity = TripTestHelper.createTripEntity(tripIdentifier);
        List<LocationEntity> locationEntities = tripEntity.getLocations();

        Trip trip = tripMapper.toTrip(tripEntity);

        List<Location> locations = trip.getLocations();
        assertThat(trip.getTripIdentifier(), is(tripEntity.getTripIdentifier()));
        assertThat(locations.size(), is(locationEntities.size()));

        IntStream.range(0, locations.size()).forEach(i -> {
            Location currentLocation = locations.get(i);
            LocationEntity currentLocationEntity = locationEntities.get(i);

            assertThat(currentLocation.getLatitude(), is(currentLocationEntity.getLatitude()));
            assertThat(currentLocation.getLongitude(), is(currentLocationEntity.getLongitude()));
            assertThat(currentLocation.getAccuracy(), is(currentLocationEntity.getAccuracy()));
        });
    }

}
