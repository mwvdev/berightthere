package mwvdev.unit;

import mwvdev.model.Trip;
import mwvdev.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        if(trip.getLocations().isEmpty()) {
            return "waiting";
        }

        model.addAttribute("tripIdentifier", trip.getTripIdentifier());
        model.addAttribute("locations", trip.getLocations());
        return "map";
    }

}
