package mwvdev.brt.repository;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository repository;

    private static final String tripIdentifier = "ab7d8df0-e952-4956-8c38-0351b90fd045";

    @Test
    void canFindTripByIdentifier() {
        TripEntity actualTripEntity = repository.findByTripIdentifier(tripIdentifier);

        assertThat(actualTripEntity.getTripIdentifier(), is(tripIdentifier));
    }

    @Test
    void canOrderLocationsByMeasuredAt() {
        TripEntity actualTripEntity = repository.findByTripIdentifier(tripIdentifier);

        LocationEntity locationEntity = actualTripEntity.getLocations().get(0);

        OffsetDateTime expectedMeasuredAt = OffsetDateTime.of(LocalDateTime.of(2020, 5, 30, 17, 31, 9), ZoneOffset.ofHours(2));
        assertThat(locationEntity.getLatitude(), is(55.6739060));
        assertThat(locationEntity.getLongitude(), is(12.5556991));
        assertThat(locationEntity.getMeasuredAt(), is(expectedMeasuredAt));
        assertThat(locationEntity.getAccuracy(), is(0.59));
    }

}