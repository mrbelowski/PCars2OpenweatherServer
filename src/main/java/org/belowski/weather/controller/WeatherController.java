package org.belowski.weather.controller;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.CreateConditions;
import org.belowski.weather.service.WeatherService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    
    @RequestMapping(path = "/data/2.5/weather", produces = "application/xml; charset=utf-8", consumes = "*/*")
    public @ResponseBody ResponseEntity<Current> getWeather(
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
            return new ResponseEntity<Current>(current, createHeaders("/data/2.5/weather", proxyAppId, latitude, longitude), HttpStatus.OK);
        }
        else {
        return new ResponseEntity<Current> (weatherService.getWeather(longitude, latitude, 
                time.isPresent() ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(time.get()), ZoneOffset.UTC) : ZonedDateTime.now(ZoneOffset.UTC)),
                        createHeaders("/data/2.5/weather", proxyAppId, latitude, longitude), HttpStatus.OK);
        }
    }
    
    @RequestMapping(path = "/weather/create", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void createWeather(
            @RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude,
            @RequestBody CreateConditions createConditions) {
        weatherService.createWeather(longitude, latitude, createConditions.getMinutesBetweenSamples(), createConditions.getConditions());
    }
    
    @RequestMapping(path = "/data/2.5/forecast", produces = "application/xml; charset=utf-8", consumes = "*/*")
    public @ResponseBody ResponseEntity<WeatherData> getForecast(
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
            return new ResponseEntity<WeatherData>(weatherData, createHeaders("/data/2.5/forecast", proxyAppId, latitude, longitude), HttpStatus.OK);
        } else {
            return new ResponseEntity<WeatherData>(                    
                    weatherService.getForecast(longitude, latitude, 8,
                    time.isPresent()
                            ? ZonedDateTime.ofInstant(Instant.ofEpochMilli(time.get()), ZoneOffset.UTC)
                            : ZonedDateTime.now(ZoneOffset.UTC)),
                    createHeaders("/data/2.5/forecast", proxyAppId, latitude, longitude), HttpStatus.OK);
        }
    }
    
    private HttpHeaders createHeaders(String endpoint, String appId, float lat, float lon) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Cache-Key", endpoint + "?APPID=" + appId + "&lat=" + lat + "&lon=" + lon + "&mode=xml");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Methods", "GET, POST");
        return headers;
    }
}
