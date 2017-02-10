package mwvdev.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import mwvdev.model.Location;
import mwvdev.model.Trip;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trip")
public class TripEntity implements Serializable, Trip {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column(nullable = false)
    private String tripIdentifier;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trip_id")
    private List<LocationEntity> locationEntities;

    protected TripEntity() {

    }

    public TripEntity(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTripIdentifier() {
        return tripIdentifier;
    }

    public void setTripIdentifier(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }


    @Override
    public List<Location> getLocations() {
        return new ArrayList<>(locationEntities);
    }


    public List<LocationEntity> getLocationEntities() {
        return locationEntities;
    }

    public void setLocationEntities(List<LocationEntity> locationEntities) {
        this.locationEntities = locationEntities;
    }

}
