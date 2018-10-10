package org.belowski.weather.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;

public interface WeatherRepository {

    /**
     * Creates a forecast if don't have one for this location + time, and returns it.
     * 
     * @param latitude
     * @param longitude     
     * @param items
     * @param minutesBetweenPoints
     * @param time
     * @return
     */
    WeatherData getForecast(float latitude, float longitude, int items, int minutesBetweenPoints, ZonedDateTime time);
    
    /**
     * Creates and returns a sample.
     * 
     * @param latitude
     * @param longitude
     * @param time
     * @return
     */
    Current getWeather(float latitude, float longitude, ZonedDateTime time);
    
    /**
     * 
     * @param latitude
     * @param longitude
     * @param minutesBetweenSamples
     * @param conditions
     */
    void createWeather(Optional<Float> latitude, Optional<Float> longitude, int minutesBetweenSamples, List<Conditions> conditions);

    /**
     * 
     * @param slotLengthMinutes
     * @param slots
     */
    void createWeatherFromSlots(int slotLengthMinutes, List<String> slots);
}
