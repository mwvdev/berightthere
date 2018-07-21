package mwvdev.brt.service.location;

import mwvdev.brt.model.Location;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TripService {

    String checkin();
    Location addLocation(String tripIdentifier, double latitude, double longitude, Double accuracy);
    List<Location> getLocations(String tripIdentifier);

}