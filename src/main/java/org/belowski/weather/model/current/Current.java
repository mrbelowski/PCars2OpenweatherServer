package org.belowski.weather.model.current;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Pressure;
import org.belowski.weather.model.Temperature;
import org.belowski.weather.service.WeatherServiceImpl;

@XmlRootElement(name = "current")
public class Current {
    
    private City city;

    private Precipitation precipitation;
        
    private Wind wind;

    private Temperature temperature;

    private Pressure pressure;

    private Humidity humidity;

    private Clouds clouds;
    
    private Visibility visibility;
    
    private LastUpdate lastUpdate;

    public Current() {
        super();
    }

    public Current(City city, Precipitation precipitation, Wind wind, Temperature temperature, Pressure pressure, Humidity humidity, Clouds clouds, Visibility visibility) {
        super();
        this.city = city;
        this.precipitation = precipitation;
        this.wind = wind;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.visibility = visibility;
        this.lastUpdate = new LastUpdate();
        this.lastUpdate.setValue(LocalDateTime.now().format(WeatherServiceImpl.DTF));
    }
    
    @XmlElement(name = "city")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @XmlElement(name = "precipitation")
    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }
    
    @XmlElement(name = "wind")
    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    @XmlElement(name = "temperature")
    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    @XmlElement(name = "pressure")
    public Pressure getPressure() {
        return pressure;
    }

    public void setPressure(Pressure pressure) {
        this.pressure = pressure;
    }

    @XmlElement(name = "humidity")
    public Humidity getHumidity() {
        return humidity;
    }

    public void setHumidity(Humidity humidity) {
        this.humidity = humidity;
    }

    @XmlElement(name = "clouds")
    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    @XmlElement(name = "visiblity")
    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    @XmlElement(name = "lastupdate")
    public LastUpdate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LastUpdate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "Current [precipitation=" + precipitation + ", wind=" + wind
                + ", temperature=" + temperature + ", pressure=" + pressure + ", humidity=" + humidity + ", clouds="
                + clouds + ", visibility=" + visibility + "]";
    }
}
