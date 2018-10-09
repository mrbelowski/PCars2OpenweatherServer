package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Pressure;
import org.belowski.weather.model.Temperature;

@XmlRootElement(name = "current")
public class Current {

    private Precipitation precipitation;
    
    private WindDirection windDirection;
    
    private Wind wind;

    private Temperature temperature;

    private Pressure pressure;

    private Humidity humidity;

    private Clouds clouds;
    
    private Visibility visibility;

    public Current() {
        super();
    }

    public Current(Precipitation precipitation, Wind wind, Temperature temperature, Pressure pressure, Humidity humidity, Clouds clouds, Visibility visibility) {
        super();
        this.precipitation = precipitation;
        this.wind = wind;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.visibility = visibility;
    }
    
    @XmlElement(name = "precipitation")
    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Precipitation precipitation) {
        this.precipitation = precipitation;
    }

    @XmlElement(name = "windDirection")
    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
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
}
