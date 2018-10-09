package org.belowski.weather.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.CreateConditions;
import org.belowski.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeatherController {
    
    @Autowired
    private WeatherService weatherService;
    
    @RequestMapping(path = "/data/2.5/weather", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody Current getWeather(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestParam(name = "time") Optional<Long> time) {
        return weatherService.getWeather(longitude, latitude, 
                time.isPresent() ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()), TimeZone.getDefault().toZoneId()) : LocalDateTime.now());
    }
    
    @RequestMapping(path = "/weather/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void createWeather(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestBody CreateConditions createConditions) {
        weatherService.createWeather(longitude, latitude, createConditions.getMinutesBetweenSamples(), createConditions.getConditions());
    }
    
    @RequestMapping(path = "/data/2.5/forecast", produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody WeatherData getForecast(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestParam(name = "time") Optional<Long> time) {
        return weatherService.getForecast(longitude, latitude, 
                time.isPresent() ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()), TimeZone.getDefault().toZoneId()) : LocalDateTime.now());
    }
    
}
