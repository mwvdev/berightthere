package mwvdev.controller;

import mwvdev.model.Location;
import mwvdev.model.SimpleLocation;
import mwvdev.model.Trip;
import mwvdev.model.TripIdentifier;
import mwvdev.repository.TripRepository;
import mwvdev.service.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class LocationController {

    private final TripRepository tripRepository;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public LocationController(TripRepository tripRepository, UuidGenerator uuidGenerator) {
        this.tripRepository = tripRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @RequestMapping("/checkin")
    public ResponseEntity<TripIdentifier> checkin() {
        TripIdentifier tripIdentifier = new TripIdentifier(uuidGenerator.generate().toString());

        tripRepository.save(new Trip(tripIdentifier.getIdentifier()));
        return new ResponseEntity<>(tripIdentifier, HttpStatus.OK);
    }

    @RequestMapping("/{tripIdentifier}/addLocation/{latitude}/{longitude}")
    public ResponseEntity addLocation(@PathVariable String tripIdentifier, @PathVariable double latitude,
                                      @PathVariable double longitude) {
        Trip trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(latitude < -90 || latitude > 90) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(longitude < -180 || longitude > 180) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        trip.getLocations().add(new Location(trip, latitude, longitude));
        tripRepository.save(trip);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/{tripIdentifier}/locations")
    public ResponseEntity<List<SimpleLocation>> getLocations(@PathVariable String tripIdentifier) {
        Trip trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        List<SimpleLocation> locations = new ArrayList<>();
        trip.getLocations().forEach(location -> locations.add(new SimpleLocation(location.getLatitude(),
                location.getLongitude())));

        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

}
