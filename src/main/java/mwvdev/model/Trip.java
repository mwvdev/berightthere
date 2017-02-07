package mwvdev.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Trip implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String tripIdentifier;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

    protected Trip() {

    }

    public Trip(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripIdentifier() {
        return tripIdentifier;
    }

    public void setTripIdentifier(String tripIdentifier) {
        this.tripIdentifier = tripIdentifier;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}
