package mwvdev.brt.repository;

import mwvdev.brt.entity.TripEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

}