package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Pressure;
import org.belowski.weather.model.Temperature;

@XmlRootElement(name = "time")
public class Time {

    private String from;

    private String to;

    private Symbol symbol;

    private Precipitation precipitation;
    
    private WindDirection windDirection;
    
    private WindSpeed windSpeed;

    private Temperature temperature;

    private Pressure pressure;

    private Humidity humidity;

    private Clouds clouds;

    public Time() {
        super();
    }

    public Time(String from, String to, Precipitation precipitation, WindDirection windDirection,
            WindSpeed windSpeed, Temperature temperature, Pressure pressure, Humidity humidity, Clouds clouds) {
        super();
        this.from = from;
        this.to = to;
        this.precipitation = precipitation;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        // now do we need to create a Symbol object?
    }

    @XmlAttribute
    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @XmlAttribute
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @XmlElement(name = "symbol")
    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
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

    @XmlElement(name = "windSpeed")
    public WindSpeed getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(WindSpeed windSpeed) {
        this.windSpeed = windSpeed;
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

    @Override
    public String toString() {
        return "Time [from=" + from + ", to=" + to + ", symbol=" + symbol + ", precipitation=" + precipitation
                + ", windDirection=" + windDirection + ", windSpeed=" + windSpeed + ", temperature=" + temperature
                + ", pressure=" + pressure + ", humidity=" + humidity + ", clouds=" + clouds + "]";
    }
}
