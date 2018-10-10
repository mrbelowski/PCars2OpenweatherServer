package org.belowski.weather.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;

public interface WeatherService {

    Current getWeather(float latitude, float longitude, ZonedDateTime time);
    
    WeatherData getForecast(float latitude, float longitude, int items, int minutesBetweenPoints, ZonedDateTime time);

    void createWeather(Optional<Float> latitude, Optional<Float> longitude, int minutesBetweenSamples, List<Conditions> conditions);
}
