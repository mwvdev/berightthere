package mwvdev.model;

import java.io.Serializable;
import java.util.List;

public interface Trip extends Serializable {

    String getTripIdentifier();

    List<Location> getLocations();

}