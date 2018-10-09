package org.belowski.weather.service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.Conditions;
import org.belowski.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public Current getWeather(float latitude, float longitude, ZonedDateTime time) {
        return weatherRepository.getWeather(latitude, longitude, time);
    }

    @Override
    public WeatherData getForecast(float latitude, float longitude, int items, ZonedDateTime time) {
        return weatherRepository.getForecast(latitude, longitude, items, time);
    }

    @Override
    public void createWeather(float latitude, float longitude, int minutesBetweenSamples, List<Conditions> conditions) {
        weatherRepository.createWeather(latitude, longitude, minutesBetweenSamples, conditions);
    }
}
