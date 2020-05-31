package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationMapperImpl implements LocationMapper {

    @Override
    public LocationEntity toEntity(long tripId, Location location) {
        return new LocationEntity(tripId, location.getLatitude(), location.getLongitude(), location.getAccuracy());
    }

    @Override
    public Location toLocation(LocationEntity locationEntity) {
        return new Location(locationEntity.getLatitude(), locationEntity.getLongitude(), locationEntity.getAccuracy());
    }

}
