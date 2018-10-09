package org.belowski.weather.service;

import java.time.LocalDateTime;
import java.util.List;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;

public interface WeatherService {

    Current getWeather(float latitude, float longitude, LocalDateTime time);
    
    WeatherData getForecast(float latitude, float longitude, int items, LocalDateTime time);

    void createWeather(float latitude, float longitude, int minutesBetweenSamples, List<Conditions> conditions);
}
