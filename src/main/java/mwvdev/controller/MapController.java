package mwvdev.controller;

import mwvdev.model.Location;
import mwvdev.model.SimpleLocation;
import mwvdev.model.Trip;
import mwvdev.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MapController {

    @Value("${brt.googleMapsKey}")
    private String googleMapsKey;

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
        if(trip.getLocations().isEmpty()) {
            return "waiting";
        }

        List<SimpleLocation> locations = new ArrayList<>();
        for(Location location : trip.getLocations()) {
            locations.add(new SimpleLocation(location.getLatitude(), location.getLongitude()));
        }
        model.addAttribute("googleMapsKey", googleMapsKey);
        model.addAttribute("locations", locations);
        return "map";
    }
    
}
