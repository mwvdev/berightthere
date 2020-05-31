package mwvdev.brt.model;

public class Location {

    private double latitude;
    private double longitude;
    private Double accuracy;

    public Location() {
    }

    public Location(double latitude, double longitude, Double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Double getAccuracy() {
        return accuracy;
    }

}
