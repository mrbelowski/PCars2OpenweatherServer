package org.belowski.weather.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.setup.CreateConditions;
import org.belowski.weather.service.WeatherService;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.oxm.Marshaller;
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

    private WeatherService weatherService;

    private RestTemplate proxyTemplate;

    private boolean proxyEnabled;

    private String proxyUrl;

    private String proxyUserAppId;

    private Marshaller responseMarshaller;
    
    private Marshaller debugMarshaller;
    
    public WeatherController(@Autowired WeatherService weatherService,
                             @Autowired RestTemplate proxyTemplate,
                             @Autowired @Qualifier("ResponseMarshaller") Marshaller responseMarshaller,
                             @Autowired @Qualifier("DebugMarshaller") Marshaller debugMarshaller,
                             @Value("${weather.proxy.enabled}") boolean proxyEnabled,
                             @Value("${weather.proxy.url}") String proxyUrl,
                             @Value("${weather.proxy.user.appId:NOT SET}") String proxyUserAppId) {
        super();
        this.weatherService = weatherService;
        this.proxyTemplate = proxyTemplate;
        this.responseMarshaller = responseMarshaller;
        this.debugMarshaller = debugMarshaller;
        this.proxyEnabled = proxyEnabled;
        this.proxyUrl = proxyUrl;
        this.proxyUserAppId = proxyUserAppId;
        if (proxyEnabled) {
            if (proxyUserAppId.equals("NOT SET")) {
                LOGGER.fatal("\n\nProxy user app ID not provided. Proxying will NOT work. Please start the app in local mode, or provide an API key with the command line argument\n"
                        + "--weather.proxy.user.appId=[your API key]\n\n");
                System.exit(1);
            }
            else {
                LOGGER.info("Running in proxy mode, using openweatherapi location " + proxyUrl + " and user's APPID " + proxyUserAppId);
            }
        } else {
            LOGGER.info("Running in local mode, weather data will be generated locally");            
        }
    }

    @RequestMapping(path = "/data/2.5/weather", produces = "application/xml; charset=utf-8", consumes = "*/*")
    public @ResponseBody ResponseEntity<String> getWeather(@RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude, @RequestParam(name = "APPID") String originalAppId,
            @RequestParam(name = "time") Optional<Long> time) throws IOException {
        Current current;
        if (proxyEnabled) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(proxyUrl + "/data/2.5/weather")
                    .queryParam("lat", latitude).queryParam("lon", longitude).queryParam("mode", "xml")
                    .queryParam("APPID", proxyUserAppId);
            current = proxyTemplate.getForEntity(builder.toUriString(), Current.class).getBody();
            LOGGER.info("got current conditions from weather server");            
        } else {            
            current = weatherService.getWeather(latitude, longitude,
                    time.isPresent() ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()), ZoneOffset.systemDefault())
                            : LocalDateTime.now());
            LOGGER.info("got generated current conditions");
        }
        String[] responseAndDebug = getResponseAndDebug(current);
        LOGGER.info(responseAndDebug[1]);
        return new ResponseEntity<String>(responseAndDebug[0],
                    createHeaders("/data/2.5/weather", originalAppId, latitude, longitude), HttpStatus.OK);
    }

    @RequestMapping(path = "/weather/create/conditions", consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public ResponseEntity<Void> createWeatherFromConditions(
            @RequestParam(name = "lat") Optional<Float> latitude,
            @RequestParam(name = "lon") Optional<Float> longitude,
            @RequestBody CreateConditions createConditions) {
        weatherService.createWeather(latitude, longitude, createConditions.getMinutesBetweenSamples(),
                createConditions.getConditions());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @RequestMapping(path = "/weather/create/slots", method = {RequestMethod.PUT, RequestMethod.GET})
    public ResponseEntity<Void> createWeatherFromSlots(
            @RequestParam(name = "lat") Optional<Float> latitude,
            @RequestParam(name = "lon") Optional<Float> longitude,
            @RequestParam(name = "slotLength") Optional<Integer> slotLengthMinutes,
            @RequestParam(name = "slot") List<String> slots) {
        weatherService.createWeatherFromSlots(latitude, longitude, slotLengthMinutes, slots);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @RequestMapping(path = "/data/2.5/forecast", produces = "application/xml; charset=utf-8", consumes = "*/*")
    public @ResponseBody ResponseEntity<String> getForecast(@RequestParam(name = "lat") float latitude,
            @RequestParam(name = "lon") float longitude, @RequestParam(name = "APPID") String originalAppId,
            @RequestParam(name = "time") Optional<Long> time,
            @RequestParam(name = "sampleCount") Optional<Integer> items,
            @RequestParam(name = "sampleLength") Optional<Integer> sampleLength) throws IOException {
        WeatherData weatherData;
        if (proxyEnabled) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(proxyUrl + "/data/2.5/forecast")
                    .queryParam("lat", latitude).queryParam("lon", longitude).queryParam("mode", "xml")
                    .queryParam("cnt", 8).queryParam("APPID", proxyUserAppId);
            weatherData = proxyTemplate.getForEntity(builder.toUriString(), WeatherData.class).getBody();
            LOGGER.info("got forecast from weather server");
        } else {
            weatherData = weatherService.getForecast(latitude, longitude, items.orElse(8), sampleLength.orElse(180),
                            time.isPresent() ? LocalDateTime.ofInstant(Instant.ofEpochMilli(time.get()), ZoneOffset.systemDefault())
                                    : LocalDateTime.now());
            LOGGER.info("got generated forecast");
        }
        String[] responseAndDebug = getResponseAndDebug(weatherData);
        LOGGER.info(responseAndDebug[1]);
        return new ResponseEntity<String>(responseAndDebug[0],
                createHeaders("/data/2.5/forecast", originalAppId, latitude, longitude), HttpStatus.OK);
    }
    
    /**
     * Eeeewwwwww....
     * 
     * This regexp spaghetti converts self-closing tags, e.g. <something a="b"/> to their
     * equivalent expanded form, e.g. <something a="b"></something>
     * 
     * It shouldn't be necessary but as we can't know how the XML is processed by the game, 
     * it's handy to ensure our output matches the service's output as close as possible.
     * 
     * @param xml
     * @return
     */
    private String postProcessXML(String xml) {
        xml = xml.replaceAll("<(\\w+)([^/>]*)?/>","<$1$2></$1>");
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xml;
    }

    private HttpHeaders createHeaders(String endpoint, String appId, float lat, float lon) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Server", "openresty");
        headers.add("X-Cache-Key", endpoint + "?APPID=" + appId + "&lat=" + lat + "&lon=" + lon + "&mode=xml");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Credentials", "true");
        headers.add("Access-Control-Allow-Methods", "GET, POST");
        headers.add("Connection", "keep-alive");
        return headers;
    }
    
    private String[] getResponseAndDebug(Object data) throws IOException {
        StringWriter responseSW = new StringWriter();
        Result responseResult = new StreamResult(responseSW);
        responseMarshaller.marshal(data, responseResult);
        StringWriter debugSW = new StringWriter();
        Result debugResult = new StreamResult(debugSW);
        debugMarshaller.marshal(data, debugResult);
        return new String[] {postProcessXML(responseSW.toString()), postProcessXML(debugSW.toString())};
    }
}
