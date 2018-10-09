package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlElement;


public class LocationWrapper {

    private String name = "NA";
    
    private String type = "";
    
    private String country = "NA";
    
    private String timezone = "";
    
    private Location location;

    public LocationWrapper() {
        super();
    }

    public LocationWrapper(Location location) {
        super();
        this.location = location;
    }

    @XmlElement(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlElement(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @XmlElement(name = "timezone")
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @XmlElement(name = "location")
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
}
