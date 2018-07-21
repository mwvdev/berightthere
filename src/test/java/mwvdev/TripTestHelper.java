package mwvdev;

import mwvdev.entity.LocationEntity;
import mwvdev.entity.TripEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class TripTestHelper {

    public static TripEntity createTripEntity(String tripIdentifier) {
        TripEntity trip = new TripEntity(tripIdentifier);
        trip.setId(42L);
        trip.setLocationEntities(createLocationEntities(trip));
        return trip;
    }

    private static List<LocationEntity> createLocationEntities(TripEntity trip) {
        return new ArrayList<>(Arrays.asList(new LocationEntity(trip.getId(), 55.6739062, 12.5556993, 7.5),
                new LocationEntity(trip.getId(), 55.6746322, 12.5585318, 5.2),
                new LocationEntity(trip.getId(), 55.6764229, 12.5588751, 3.3)));
    }

    private TripTestHelper() {

    }
}