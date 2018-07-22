package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
public class LocationMapperTest {

    private LocationMapper locationMapper;

    @Before
    public void setUp() {
        locationMapper = new LocationMapperImpl();
    }

    @Test
    public void canMapToLocation() {
        LocationEntity locationEntity = new LocationEntity(42L, 55.6739062, 12.5556993, 0.75);

        Location location = locationMapper.toLocation(locationEntity);

        assertThat(location.getLatitude(), is(locationEntity.getLatitude()));
        assertThat(location.getLongitude(), is(locationEntity.getLongitude()));
        assertThat(location.getAccuracy(), is(locationEntity.getAccuracy()));
    }

}
