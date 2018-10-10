package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.belowski.weather.model.Sun;

@XmlRootElement(name = "weatherdata")
@XmlType(propOrder={"locationWrapper", "credit", "meta", "sun", "forecast"})
public class WeatherData {

    private Sun sun;
    
    private LocationWrapper locationWrapper;
    
    private Meta meta = new Meta();
    
    private Credit credit = new Credit();
    
    private Forecast forecast;

    public WeatherData() {
        super();
    }

    public WeatherData(Sun sun, LocationWrapper locationWrapper, Forecast forecast) {
        super();
        this.sun = sun;
        this.locationWrapper = locationWrapper;
        this.forecast = forecast;
    }
    
    @XmlElement(name = "location")
    public LocationWrapper getLocationWrapper() {
        return locationWrapper;
    }

    public void setLocationWrapper(LocationWrapper locationWrapper) {
        this.locationWrapper = locationWrapper;
    }

    @XmlElement(name = "sun")
    public Sun getSun() {
        return sun;
    }

    public void setSun(Sun sun) {
        this.sun = sun;
    }

    @XmlElement(name = "forecast")
    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    @XmlElement(name = "meta")
    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    @XmlElement(name = "credit")
    public Credit getCredit() {
        return credit;
    }

    public void setCredit(Credit credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "WeatherData [sun=" + sun + ", forecast=" + forecast + "]";
    }
}
