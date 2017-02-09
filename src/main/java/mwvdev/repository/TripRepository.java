package mwvdev.repository;

import mwvdev.entity.TripEntity;
import mwvdev.model.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Trip, Long> {

    TripEntity findByTripIdentifier(String tripIdentifier);

}
