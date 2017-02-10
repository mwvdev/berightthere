package mwvdev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import mwvdev.model.Location;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "location")
public class LocationEntity implements Serializable, Location {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(name = "trip_id")
    @JsonIgnore
    private long tripId;

    private double latitude;
    private double longitude;

    protected LocationEntity() {

    }

    public LocationEntity(long tripId, double latitude, double longitude) {
        this.tripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTripId() {
        return tripId;
    }


    public void setTripId(long tripId) {
        this.tripId = tripId;
    }


    @Override
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
