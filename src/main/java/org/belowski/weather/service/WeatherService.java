package org.belowski.weather.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;

public interface WeatherService {

    Current getWeather(float latitude, float longitude, LocalDateTime time);
    
    WeatherData getForecast(float latitude, float longitude, int items, int minutesBetweenPoints, LocalDateTime time);

    void createWeather(Optional<Float> latitude, Optional<Float> longitude, int minutesBetweenSamples, List<Conditions> conditions);

    void createWeatherFromSlots(Optional<Float> latitude, Optional<Float> longitude, Optional<Integer> slotLengthMinutes, List<String> slots);
}
