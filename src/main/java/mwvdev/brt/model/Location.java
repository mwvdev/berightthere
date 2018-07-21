package mwvdev.brt.model;

import java.io.Serializable;

public interface Location extends Serializable {

    double getLatitude();

    double getLongitude();

    Double getAccuracy();

}