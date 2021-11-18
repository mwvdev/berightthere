package mwvdev.brt.service.mapper;

import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.model.TripImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripMapperImpl implements TripMapper {

    private final LocationMapper locationMapper;

    @Autowired
    public TripMapperImpl(LocationMapper locationMapper) {
        this.locationMapper = locationMapper;
    }

    @Override
    public Trip toTrip(TripEntity tripEntity) {
        List<Location> locations = tripEntity
                .getLocations()
                .stream()
                .map(locationMapper::toLocation)
                .collect(Collectors.toList());

        return new TripImpl(tripEntity.getTripIdentifier(), locations);
    }

}
