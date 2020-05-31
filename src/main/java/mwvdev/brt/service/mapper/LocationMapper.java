package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;

public interface LocationMapper {

    LocationEntity toEntity(long tripId, Location location);

    Location toLocation(LocationEntity locationEntity);

}
