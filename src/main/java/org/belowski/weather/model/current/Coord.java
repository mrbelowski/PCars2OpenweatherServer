package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"longitude", "latitude"})
public class Coord {

    private float latitude;
    
    private float longitude;

    public Coord(float latitude, float longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coord() {
        super();
        // TODO Auto-generated constructor stub
    }

    @XmlAttribute(name = "lat")
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    @XmlAttribute(name = "lon")
    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
