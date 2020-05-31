package mwvdev.brt.service.mapper;

import mwvdev.brt.TripTestHelper;
import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class LocationMapperTest {

    private LocationMapper locationMapper;

    @BeforeEach
    void setUp() {
        locationMapper = new LocationMapperImpl();
    }

    @Test
    void canMapToEntity() {
        long tripId = 42L;
        Location location = TripTestHelper.createReferenceLocation();

        LocationEntity locationEntity = locationMapper.toEntity(tripId, location);

        assertThat(locationEntity.getTripId(), is(tripId));
        assertThat(locationEntity.getLatitude(), is(location.getLatitude()));
        assertThat(locationEntity.getLongitude(), is(location.getLongitude()));
        assertThat(locationEntity.getAccuracy(), is(location.getAccuracy()));
    }

    @Test
    void canMapToLocation() {
        LocationEntity locationEntity = TripTestHelper.createReferenceLocationEntity();

        Location location = locationMapper.toLocation(locationEntity);

        assertThat(location.getLatitude(), is(locationEntity.getLatitude()));
        assertThat(location.getLongitude(), is(locationEntity.getLongitude()));
        assertThat(location.getAccuracy(), is(locationEntity.getAccuracy()));
    }

}
