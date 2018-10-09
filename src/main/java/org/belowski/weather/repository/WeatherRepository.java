package org.belowski.weather.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;

public interface WeatherRepository {

    /**
     * Creates a forecast if don't have one for this location + time, and returns it.
     * 
     * @param latitude
     * @param longitude     
     * @param time
     * @return
     */
    WeatherData getForecast(float latitude, float longitude, LocalDateTime time);
    
    /**
     * Creates and returns a sample.
     * 
     * @param latitude
     * @param longitude
     * @param time
     * @return
     */
    Current getWeather(float latitude, float longitude, LocalDateTime time);
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @param minutesBetweenSamples
     * @param conditions
     */
    void createWeather(float latitude, float longitude, int minutesBetweenSamples, List<Conditions> conditions);
}
