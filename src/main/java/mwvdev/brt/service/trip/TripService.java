package mwvdev.brt.service.trip;

import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import org.springframework.stereotype.Service;

@Service
public interface TripService {

    String checkin();
    void addLocation(String tripIdentifier, Location location);
    Trip getTrip(String tripIdentifier);

}