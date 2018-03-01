package mwvdev.controller;

import mwvdev.entity.LocationEntity;
import mwvdev.entity.TripEntity;
import mwvdev.model.Location;
import mwvdev.model.Trip;
import mwvdev.model.TripIdentifier;
import mwvdev.repository.TripRepository;
import mwvdev.service.UuidGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trip")
public class LocationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final TripRepository tripRepository;
    private final UuidGenerator uuidGenerator;

    @Autowired
    public LocationController(SimpMessagingTemplate simpMessagingTemplate,
                              TripRepository tripRepository, UuidGenerator uuidGenerator) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.tripRepository = tripRepository;
        this.uuidGenerator = uuidGenerator;
    }

    @RequestMapping("/checkin")
    public ResponseEntity<TripIdentifier> checkin() {
        TripIdentifier tripIdentifier = new TripIdentifier(uuidGenerator.generate().toString());

        tripRepository.save(new TripEntity(tripIdentifier.getIdentifier()));
        return new ResponseEntity<>(tripIdentifier, HttpStatus.OK);
    }

    @RequestMapping("/{tripIdentifier}/addLocation/{latitude}/{longitude}")
    public ResponseEntity addLocation(@PathVariable String tripIdentifier,
                                      @PathVariable double latitude,
                                      @PathVariable double longitude,
                                      @RequestParam(required = false) Double accuracy) {
        TripEntity trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        if(latitude < -90 || latitude > 90) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if(longitude < -180 || longitude > 180) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        LocationEntity location = new LocationEntity(trip.getId(), latitude, longitude, accuracy);
        trip.getLocationEntities().add(location);
        tripRepository.save(trip);

        simpMessagingTemplate.convertAndSend("/topic/" + tripIdentifier, location);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("/{tripIdentifier}/locations")
    public ResponseEntity<List<Location>> getLocations(@PathVariable String tripIdentifier) {
        Trip trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(trip.getLocations(), HttpStatus.OK);
    }

}
