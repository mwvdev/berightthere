package mwvdev.model;

public class SimpleLocation {

    private final double latitude;
    private final double longitude;

    public SimpleLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
