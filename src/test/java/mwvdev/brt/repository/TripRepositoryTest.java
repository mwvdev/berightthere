package mwvdev.brt.repository;

import mwvdev.brt.entity.TripEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TripRepositoryTest {

    @Autowired
    private TripRepository repository;

    private static final String tripIdentifier = "ab7d8df0-e952-4956-8c38-0351b90fd045";

    @Test
    public void canFindTripByIdentifier() {
        TripEntity actualTripEntity = repository.findByTripIdentifier(tripIdentifier);

        assertThat(actualTripEntity.getTripIdentifier(), is(tripIdentifier));
    }

}