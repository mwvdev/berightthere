package mwvdev.controller;

import mwvdev.model.Location;
import mwvdev.service.location.TripService;
import mwvdev.service.location.UnknownTripException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Controller
public class BrokerController {

    private TripService tripService;

    @Autowired
    public BrokerController(TripService tripService) {
        this.tripService = tripService;
    }

    @SubscribeMapping("trip.{tripIdentifier}.locations")
    @Transactional
    public Collection<Location> getLocations(@DestinationVariable("tripIdentifier") String tripIdentifier) {
        try {
            return tripService.getLocations(tripIdentifier);
        }
        catch(UnknownTripException e) {
            return Collections.emptyList();
        }
    }

}