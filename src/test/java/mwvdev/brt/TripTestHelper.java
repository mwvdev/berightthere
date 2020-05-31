package mwvdev.brt;

import mwvdev.brt.entity.LocationEntity;
import mwvdev.brt.entity.TripEntity;
import mwvdev.brt.model.Location;
import mwvdev.brt.model.Trip;
import mwvdev.brt.model.TripImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TripTestHelper {

    public static Trip createTrip(String tripIdentifier) {
        return new TripImpl(tripIdentifier, createLocations());
    }

    private static List<Location> createLocations() {
        return new ArrayList<>(Arrays.asList(new Location(55.6739062, 12.5556993, 7.5),
                new Location(55.6746322, 12.5585318, 5.2),
                new Location(55.6764229, 12.5588751, 3.3)));
    }

    public static TripEntity createTripEntity(String tripIdentifier) {
        TripEntity trip = new TripEntity(tripIdentifier);
        trip.setId(42L);
        trip.setLocations(createLocationEntities(trip));
        return trip;
    }

    private static List<LocationEntity> createLocationEntities(TripEntity trip) {
        return new ArrayList<>(Arrays.asList(new LocationEntity(trip.getId(), 55.6739062, 12.5556993, 7.5),
                new LocationEntity(trip.getId(), 55.6746322, 12.5585318, 5.2),
                new LocationEntity(trip.getId(), 55.6764229, 12.5588751, 3.3)));
    }

    public static LocationEntity createReferenceLocationEntity() {
        return new LocationEntity(42L, 55.6764233, 12.5588757, 3.9);
    }

    public static Location createReferenceLocation() {
        return new Location(55.6764233, 12.5588757, 3.9);
    }

    private TripTestHelper() {

    }

}