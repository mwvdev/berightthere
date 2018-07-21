package mwvdev.brt.service.location;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.repository.TripRepository;
import mwvdev.brt.service.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {

    private final TripRepository tripRepository;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public TripServiceImpl(TripRepository tripRepository, UuidGenerator uuidGenerator) {
        this.tripRepository = tripRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @Override
    public String checkin() {
        String tripIdentifier = uuidGenerator.generate().toString();

        tripRepository.save(new TripEntity(tripIdentifier));

        return tripIdentifier;
    }

    @Override
    public Location addLocation(String tripIdentifier, double latitude, double longitude, Double accuracy) {
        TripEntity trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            throw new UnknownTripException();
        }

        LocationEntity location = new LocationEntity(trip.getId(), latitude, longitude, accuracy);
        trip.getLocationEntities().add(location);
        tripRepository.save(trip);

        return location;
    }

    @Override
    public List<Location> getLocations(String tripIdentifier) {
        Trip trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            throw new UnknownTripException();
        }

        return trip.getLocations();
    }

}