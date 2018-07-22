package mwvdev.brt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "trip")
public class TripEntity implements Serializable {

    private Long id;
    private String tripIdentifier;
    private List<LocationEntity> locations;

    protected TripEntity() {

    }

    public TripEntity(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
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

    @Column(nullable = false)
    public String getTripIdentifier() {
        return tripIdentifier;
    }

    public void setTripIdentifier(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "trip_id")
    public List<LocationEntity> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationEntity> locations) {
        this.locations = locations;
    }

}