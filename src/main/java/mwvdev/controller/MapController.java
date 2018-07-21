package mwvdev.controller;

import mwvdev.model.Location;
import mwvdev.model.Trip;
import mwvdev.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MapController {

    private final TripRepository tripRepository;

    @Autowired
    public MapController(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @RequestMapping("/trip/{tripIdentifier}")
    public String viewTrip(@PathVariable String tripIdentifier, Model model) {
        Trip trip = tripRepository.findByTripIdentifier(tripIdentifier);
        if(trip == null) {
            return "unknown";
        }

        List<Location> locations = trip.getLocations();
        if(locations.isEmpty()) {
            return "waiting";
        }

        model.addAttribute("tripIdentifier", trip.getTripIdentifier());
        model.addAttribute("locations", locations);
        model.addAttribute("latestLocation", locations.get(locations.size() - 1));
        return "map";
    }

}