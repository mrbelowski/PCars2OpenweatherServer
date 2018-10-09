package org.belowski.weather.model.setup;

import java.time.LocalDateTime;

/**
 * Used to set weather
 * 
 * @author Jim
 *
 */
public class Conditions {

    private LocalDateTime time;
    
    private float temperature;
    
    private float precipitation;
    
    private float pressure;
    
    private int humidity;
    
    private int clouds;
    
    private float windSpeed;
    
    private int windDirection;
    
    private int visibility;
    
    public Conditions() {
        super();
    }

    public Conditions(LocalDateTime time, float temperature, float precipitation, float pressure, int humidity,
            int clouds, float windSpeed, int windDirection, int visibility) {
        super();
        this.time = time;
        this.temperature = temperature;
        this.precipitation = precipitation;
        this.pressure = pressure;
        this.humidity = humidity;
        this.clouds = clouds;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.visibility = visibility;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(float precipitation) {
        this.precipitation = precipitation;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getClouds() {
        return clouds;
    }

    public void setClouds(int clouds) {
        this.clouds = clouds;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Conditions cloneForTime(LocalDateTime start) {
        return new Conditions(start, this.temperature, this.precipitation, this.pressure, this.humidity, this.clouds, this.windSpeed, this.windDirection, this.visibility);
    }

    @Override
    public String toString() {
        return "Conditions [time=" + time + ", temperature=" + temperature + ", precipitation=" + precipitation
                + ", pressure=" + pressure + ", humidity=" + humidity + ", clouds=" + clouds + ", windSpeed="
                + windSpeed + ", windDirection=" + windDirection + ", visibility=" + visibility + "]";
    }
}
