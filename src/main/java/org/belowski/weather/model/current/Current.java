package org.belowski.weather.model.current;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.service.WeatherServiceImpl;

@XmlRootElement(name = "current")
@XmlType(propOrder={"city", "temperature", "humidity", "pressure", "wind", "clouds", "visibility", "precipitation", "weather", "lastUpdate"})
public class Current {
    
    private City city;

    private CurrentPrecipitation precipitation;
        
    private Wind wind;

    private CurrentTemperature temperature;

    private CurrentPressure pressure;

    private Humidity humidity;

    private CurrentClouds clouds;
    
    private Visibility visibility;
    
    private LastUpdate lastUpdate;
    
    private Weather weather;    
    
    public Current() {
        super();
    }
    public Current(Weather weather) {
        this.weather = weather;
    }
    
    public Current(City city, CurrentPrecipitation precipitation, Wind wind, CurrentTemperature temperature, CurrentPressure pressure, 
            Humidity humidity, CurrentClouds clouds, Visibility visibility, Weather weather) {
        super();
        this.city = city;
        this.precipitation = precipitation;
        this.wind = wind;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.lastUpdate = new LastUpdate();
        this.lastUpdate.setValue(ZonedDateTime.now(ZoneOffset.UTC).format(WeatherServiceImpl.DTF));
        this.visibility = visibility;
        this.weather = weather;
    }
    
    @XmlElement(name = "city")
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @XmlElement(name = "precipitation")
    public CurrentPrecipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(CurrentPrecipitation precipitation) {
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
    public CurrentTemperature getTemperature() {
        return temperature;
    }

    public void setTemperature(CurrentTemperature temperature) {
        this.temperature = temperature;
    }

    @XmlElement(name = "pressure")
    public CurrentPressure getPressure() {
        return pressure;
    }

    public void setPressure(CurrentPressure pressure) {
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
    public CurrentClouds getClouds() {
        return clouds;
    }

    public void setClouds(CurrentClouds clouds) {
        this.clouds = clouds;
    }
    
    @XmlElement(name = "lastupdate")
    public LastUpdate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LastUpdate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    @XmlElement(name = "visibility")
    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    @XmlElement(name = "weather")
    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "Current [conditionType= " + weather.getConditionType() + "]";
    }
}
