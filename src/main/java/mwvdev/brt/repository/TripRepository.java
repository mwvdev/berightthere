package mwvdev.brt.repository;

import mwvdev.brt.entity.TripEntity;
import org.springframework.data.repository.CrudRepository;

public interface TripRepository extends CrudRepository<TripEntity, Long> {

    TripEntity findByTripIdentifier(String tripIdentifier);

}