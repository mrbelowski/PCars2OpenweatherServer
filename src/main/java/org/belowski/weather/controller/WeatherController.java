package org.belowski.weather.controller;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.TimeZone;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.CreateConditions;
import org.belowski.weather.service.WeatherService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class WeatherController {
    
    private static final Logger LOGGER = Logger.getLogger(WeatherController.class.getName());
        
    @Autowired
    private WeatherService weatherService;
    
    @Autowired
    private RestTemplate proxyTemplate;

    @Value("${proxy.enabled}")
    private boolean proxyEnabled;

    @Value("${proxy.url}")
    private String proxyUrl;

    @Value("${proxy.appId}")
    private String proxyAppId;
    
    @RequestMapping(path = "/data/2.5/weather", produces = MediaType.APPLICATION_XML_VALUE, consumes = "*/*")
    public @ResponseBody Current getWeather(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestParam(name = "time") Optional<Long> time) {
        if (proxyEnabled) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(proxyUrl + "/data/2.5/weather")
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude)
                    .queryParam("mode", "xml")
                    .queryParam("APPID", proxyAppId);
            Current current = proxyTemplate.getForEntity(builder.toUriString(), Current.class).getBody();
            LOGGER.info("got current conditions from weather server: " + current.toString());
            return current;
        }
        else {
        return weatherService.getWeather(longitude, latitude, 
                time.isPresent() ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()), TimeZone.getDefault().toZoneId()) : LocalDateTime.now());
        }
    }
    
    @RequestMapping(path = "/weather/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void createWeather(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestBody CreateConditions createConditions) {
        weatherService.createWeather(longitude, latitude, createConditions.getMinutesBetweenSamples(), createConditions.getConditions());
    }
    
    @RequestMapping(path = "/data/2.5/forecast", produces = MediaType.APPLICATION_XML_VALUE, consumes = "*/*")
    public @ResponseBody WeatherData getForecast(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestParam(name = "time") Optional<Long> time) {
        if (proxyEnabled) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(proxyUrl + "/data/2.5/forecast")
                    .queryParam("lat", latitude)
                    .queryParam("lon", longitude)
                    .queryParam("mode", "xml")
                    .queryParam("cnt", 8)
                    .queryParam("APPID", proxyAppId);
            WeatherData weatherData = proxyTemplate.getForEntity(builder.toUriString(), WeatherData.class).getBody();
            LOGGER.info("Got forecast: " + weatherData);
            return weatherData;
        } else {
            return weatherService.getForecast(longitude, latitude, 8,
                    time.isPresent()
                            ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()),
                                    TimeZone.getDefault().toZoneId())
                            : LocalDateTime.now());
        }
    }
}
