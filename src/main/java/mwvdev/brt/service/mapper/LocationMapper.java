package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;

public interface LocationMapper {

    Location toLocation(LocationEntity locationEntity);

}
