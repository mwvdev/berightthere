package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.LocationImpl;
import org.springframework.stereotype.Service;

@Service
public class LocationMapperImpl implements LocationMapper {

    @Override
    public Location toLocation(LocationEntity locationEntity) {
        return new LocationImpl(locationEntity.getLatitude(), locationEntity.getLongitude(), locationEntity.getAccuracy());
    }

}
