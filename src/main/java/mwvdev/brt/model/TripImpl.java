package mwvdev.brt.model;

import java.util.List;

public class TripImpl implements Trip {

    private final String tripIdentifier;
    private final List<Location> locations;

    public TripImpl(String tripIdentifier, List<Location> locations) {
        this.tripIdentifier = tripIdentifier;
        this.locations = locations;
    }

    @Override
    public String getTripIdentifier() {
        return tripIdentifier;
    }

    @Override
    public List<Location> getLocations() {
        return locations;
    }

}
