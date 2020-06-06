package mwvdev.brt.model;

import java.time.OffsetDateTime;

public class Location {

    private double latitude;
    private double longitude;
    private OffsetDateTime measuredAt;
    private Double accuracy;

    protected Location() {
    }

    public Location(double latitude, double longitude, OffsetDateTime measuredAt, Double accuracy) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.measuredAt = measuredAt;
        this.accuracy = accuracy;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public OffsetDateTime getMeasuredAt() {
        return measuredAt;
    }

    public Double getAccuracy() {
        return accuracy;
    }

}
