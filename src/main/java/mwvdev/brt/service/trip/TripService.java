package mwvdev.brt.service.trip;

import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import org.springframework.stereotype.Service;

@Service
public interface TripService {

    String checkin();
    Location addLocation(String tripIdentifier, double latitude, double longitude, Double accuracy);
    Trip getTrip(String tripIdentifier);

}