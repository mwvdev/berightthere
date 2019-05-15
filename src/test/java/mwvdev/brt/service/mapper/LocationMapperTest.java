package mwvdev.brt.service.mapper;

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
    void canMapToLocation() {
        LocationEntity locationEntity = new LocationEntity(42L, 55.6739062, 12.5556993, 0.75);

        Location location = locationMapper.toLocation(locationEntity);

        assertThat(location.getLatitude(), is(locationEntity.getLatitude()));
        assertThat(location.getLongitude(), is(locationEntity.getLongitude()));
        assertThat(location.getAccuracy(), is(locationEntity.getAccuracy()));
    }

}
