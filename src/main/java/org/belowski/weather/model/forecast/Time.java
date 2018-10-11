package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.belowski.weather.model.Humidity;

@XmlRootElement(name = "time")
@XmlType(propOrder={"symbol", "precipitation", "windDirection", "windSpeed", "temperature", "pressure", "humidity", "clouds"})
public class Time {

    private String from;

    private String to;

    private Symbol symbol;

    private ForecastPrecipitation precipitation;
    
    private WindDirection windDirection;
    
    private WindSpeed windSpeed;

    private ForecastTemperature temperature;

    private ForecastPressure pressure;

    private Humidity humidity;

    private ForecastClouds clouds;

    public Time() {
        super();
    }

    public Time(String from, String to, Symbol symbol) {
        super();
        this.from = from;
        this.to = to;
        this.symbol = symbol;
    }
    
    public Time(String from, String to, ForecastPrecipitation precipitation, WindDirection windDirection,
            WindSpeed windSpeed, ForecastTemperature temperature, ForecastPressure pressure, Humidity humidity, ForecastClouds clouds,
            float rainNumber, int visibility) {
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
        this.symbol = Symbol.generate(rainNumber, visibility, this.clouds.getAll());
    }
    
    public Time(String from, String to, ForecastPrecipitation precipitation, WindDirection windDirection,
            WindSpeed windSpeed, ForecastTemperature temperature, ForecastPressure pressure, Humidity humidity, ForecastClouds clouds,
            Symbol symbol) {
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
        this.symbol = symbol;
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
    public ForecastPrecipitation getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(ForecastPrecipitation precipitation) {
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
    public ForecastTemperature getTemperature() {
        return temperature;
    }

    public void setTemperature(ForecastTemperature temperature) {
        this.temperature = temperature;
    }

    @XmlElement(name = "pressure")
    public ForecastPressure getPressure() {
        return pressure;
    }

    public void setPressure(ForecastPressure pressure) {
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
    public ForecastClouds getClouds() {
        return clouds;
    }

    public void setClouds(ForecastClouds clouds) {
        this.clouds = clouds;
    }
    
    public Time cloneToNewPeriod(String from, String to) {
        return new Time(from, to, this.precipitation, this.windDirection, this.windSpeed, 
                this.temperature, this.pressure, this.humidity, this.clouds, this.symbol);
    }

    @Override
    public String toString() {
        return "Time [from=" + from + ", to=" + to + ", conditionType= " + symbol.getConditionType() + "]";
    }
}
