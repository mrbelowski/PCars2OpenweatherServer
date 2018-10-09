package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.belowski.weather.model.Sun;

public class City {

    private String id = "1234567";
    
    private String name = "Unknown";
    
    private Coord coord;
    
    private String country = "NA";
    
    private Sun sun;
    
    public City(Coord coord, Sun sun) {
        super();
        this.coord = coord;
        this.country = country;
        this.sun = sun;
    }

    public City() {
        super();
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "coord")
    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    @XmlElement(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlElement(name = "sun")
    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }
    
    
}
