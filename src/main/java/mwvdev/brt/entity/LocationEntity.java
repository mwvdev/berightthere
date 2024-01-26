package mwvdev.brt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "location")
public class LocationEntity implements Serializable {

    private Long id;
    private long tripId;
    private double latitude;
    private double longitude;
    private OffsetDateTime measuredAt;
    private Double accuracy;

    protected LocationEntity() {
    }

    public LocationEntity(long tripId, double latitude, double longitude, OffsetDateTime measuredAt, Double accuracy) {
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.measuredAt = measuredAt;
        this.accuracy = accuracy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "trip_id")
    @JsonIgnore
    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public OffsetDateTime getMeasuredAt() {
        return measuredAt;
    }

    public void setMeasuredAt(OffsetDateTime measuredAt) {
        this.measuredAt = measuredAt;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

}