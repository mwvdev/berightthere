package mwvdev.brt.service.trip;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.repository.TripRepository;
import mwvdev.brt.service.UuidGenerator;
import mwvdev.brt.service.mapper.LocationMapper;
import mwvdev.brt.service.mapper.TripMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final LocationMapper locationMapper;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, TripMapper tripMapper, LocationMapper locationMapper, UuidGenerator uuidGenerator) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
        this.locationMapper = locationMapper;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public String checkin() {
        String tripIdentifier = uuidGenerator.generate().toString();

        tripRepository.save(new TripEntity(tripIdentifier));

        return tripIdentifier;
    }

    @Override
    public void addLocation(String tripIdentifier, Location location) {
        TripEntity trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            throw new UnknownTripException();
        }

        LocationEntity locationEntity = locationMapper.toEntity(trip.getId(), location);
        trip.getLocations().add(locationEntity);
        tripRepository.save(trip);
    }

    @Override
    public Trip getTrip(String tripIdentifier) {
        TripEntity tripEntity = tripRepository.findByTripIdentifier(tripIdentifier);
        if(tripEntity == null) {
            throw new UnknownTripException();
        }

        return tripMapper.toTrip(tripEntity);
    }

}