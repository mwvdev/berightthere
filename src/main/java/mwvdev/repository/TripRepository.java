package mwvdev.repository;

import mwvdev.entity.TripEntity;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<TripEntity, Long> {

    TripEntity findByTripIdentifier(String tripIdentifier);

}