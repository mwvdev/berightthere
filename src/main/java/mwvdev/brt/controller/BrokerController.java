package mwvdev.brt.controller;

import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Controller
public class BrokerController {

    private final TripService tripService;

    @Autowired
    public BrokerController(TripService tripService) {
        this.tripService = tripService;
    }

    @SubscribeMapping("trip.{tripIdentifier}.locations")
    @Transactional
    public Collection<Location> getLocations(@DestinationVariable("tripIdentifier") String tripIdentifier) {
        try {
            Trip trip = tripService.getTrip(tripIdentifier);
            return trip.getLocations();
        }
        catch(UnknownTripException e) {
            return Collections.emptyList();
        }
    }

}