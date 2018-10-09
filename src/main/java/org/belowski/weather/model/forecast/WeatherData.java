package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlElement;

public class WeatherData {

    private Sun sun;
    
    private Forecast forecast;

    public WeatherData() {
        super();
    }

    public WeatherData(Sun sun, Forecast forecast) {
        super();
        this.sun = sun;
        this.forecast = forecast;
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
}
