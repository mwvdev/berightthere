package mwvdev.brt.controller;

import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.model.TripIdentifier;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/trip")
public class TripController {

    private final TripService tripService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public TripController(TripService tripService,
                          SimpMessagingTemplate simpMessagingTemplate) {
        this.tripService = tripService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @RequestMapping("/checkin")
    public ResponseEntity<TripIdentifier> checkin() {

        TripIdentifier tripIdentifier = new TripIdentifier(tripService.checkin());
        return new ResponseEntity<>(tripIdentifier, HttpStatus.OK);
    }

    @PostMapping("/{tripIdentifier}/addLocation")
    public ResponseEntity<Void> addLocation(@PathVariable String tripIdentifier,
                                            @RequestBody Location location) {
        if(location.getLatitude() < -90 || location.getLatitude() > 90) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(location.getLongitude() < -180 || location.getLongitude() > 180) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            tripService.addLocation(tripIdentifier, location);
        }
        catch (UnknownTripException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        simpMessagingTemplate.convertAndSend("/topic/" + tripIdentifier, location);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping("/{tripIdentifier}/locations")
    @Transactional
    public ResponseEntity<Collection<Location>> getLocations(@PathVariable String tripIdentifier) {
        try {
            Trip trip = tripService.getTrip(tripIdentifier);
            return new ResponseEntity<>(trip.getLocations(), HttpStatus.OK);
        }
        catch(UnknownTripException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}