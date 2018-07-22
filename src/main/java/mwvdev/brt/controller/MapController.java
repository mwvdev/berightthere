package mwvdev.brt.controller;

import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.service.trip.TripService;
import mwvdev.brt.service.trip.UnknownTripException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MapController {

    private final TripService tripService;

    @Autowired
    public MapController(TripService tripService) {
        this.tripService = tripService;
    }

    @RequestMapping("/trip/{tripIdentifier}")
    public String viewTrip(@PathVariable String tripIdentifier, Model model) {
        Trip trip;
        try {
            trip = tripService.getTrip(tripIdentifier);
        }
        catch(UnknownTripException e) {
            return "unknown";
        }

        List<Location> locations = trip.getLocations();
        if(locations.isEmpty()) {
            return "waiting";
        }

        model.addAttribute("tripIdentifier", tripIdentifier);
        model.addAttribute("locations", locations);
        model.addAttribute("latestLocation", locations.get(locations.size() - 1));
        return "map";
    }

}