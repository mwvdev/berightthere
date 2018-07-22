package mwvdev.brt.model;

public class LocationImpl implements Location {

    private final double latitude;
    private final double longitude;
    private final Double accuracy;

    public LocationImpl(double latitude, double longitude, Double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public Double getAccuracy() {
        return accuracy;
    }

}
