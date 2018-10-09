package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.belowski.weather.model.Sun;

@XmlRootElement(name = "weatherdata")
public class WeatherData {

    private Sun sun;
    
    private LocationWrapper locationWrapper;
    
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

    @Override
    public String toString() {
        return "WeatherData [sun=" + sun + ", forecast=" + forecast + "]";
    }
}
