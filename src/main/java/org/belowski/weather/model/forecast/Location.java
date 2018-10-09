package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

public class Location {

    private float altitude = 0;
    
    private float latitude;
    
    private float longitude;
    
    public Location() {
        super();
    }

    public Location(float latitude, float longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @XmlAttribute(name = "altitude")
    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    @XmlAttribute(name = "latitude")
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @XmlAttribute(name = "longitude")
    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}