package mwvdev.repository;

import mwvdev.model.Trip;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<Trip, Long> {

    Trip findByTripIdentifier(String tripIdentifier);

}
