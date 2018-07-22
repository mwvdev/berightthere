package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Trip;

public interface TripMapper {

    Trip toTrip(TripEntity tripEntity);

}
