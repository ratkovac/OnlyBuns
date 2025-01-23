package dto;

import java.io.Serializable;

public class LocationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private double latitude;
    private double longitude;

    // Konstruktor, getteri i setteri
    public LocationDto() {}

    public LocationDto(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
