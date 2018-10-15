package org.belowski.weather.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Sun;
import org.belowski.weather.model.ConditionsConstants;
import org.belowski.weather.model.ConditionsConstants.ConditionType;
import org.belowski.weather.model.current.City;
import org.belowski.weather.model.current.CurrentClouds;
import org.belowski.weather.model.current.Coord;
import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.current.Direction;
import org.belowski.weather.model.current.CurrentPressure;
import org.belowski.weather.model.current.CurrentPrecipitation;
import org.belowski.weather.model.current.Speed;
import org.belowski.weather.model.current.CurrentTemperature;
import org.belowski.weather.model.current.Visibility;
import org.belowski.weather.model.current.Weather;
import org.belowski.weather.model.current.Wind;
import org.belowski.weather.model.forecast.Forecast;
import org.belowski.weather.model.forecast.ForecastPressure;
import org.belowski.weather.model.forecast.ForecastTemperature;
import org.belowski.weather.model.forecast.LocationWrapper;
import org.belowski.weather.model.forecast.Symbol;
import org.belowski.weather.model.forecast.Time;
import org.belowski.weather.model.forecast.WeatherData;
import org.belowski.weather.model.forecast.WindDirection;
import org.belowski.weather.model.forecast.WindSpeed;
import org.belowski.weather.model.setup.Conditions;
import org.belowski.weather.model.setup.Location;
import org.belowski.weather.service.WeatherServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class WeatherRepositoryImpl implements WeatherRepository {
    
    private static final Logger LOGGER = Logger.getLogger(WeatherRepositoryImpl.class.getName());
    
    public static Location ANY_LOCATION = Location.create(9999f, 9999f);
    
    private enum ChangeType { IMPROVING, WORSENING };
    
    private static final Random random = new Random();
    
    private int hoursToCreate = 28;
    private int samplesPerHour = 6;
    private int secondsForFullRainTransition = 30 * 60;  // rain changes take this many seconds to go from 0 (dry) to 1 (monsoon)
    private int secondsForFullWindTransition = 120 * 60; // wind changes take this many seconds to change from min to max
    private int secondsForFullTempTransition = 120 * 60;   // temp changes take this many seconds to change from min to max
    
    private float minTemp = 283f;
    private float maxTemp = 313f;
    private float maxRain = 1f;
    private float minWindSpeed = 0f;
    private float maxWindSpeed = 30f;
    private float minPressure = 980f;
    private float maxPressure = 1020f;
    private int minTimeBeforeChangingWeatherDirection = 20 * 60;    // don't change from improving to worsening before 20 minutes
    private int maxTimeBeforeChangingWeatherDirection = 60 * 120;    // don't allow weather to continue worsening for > 2 hours
    
    private Map<Location, List<Conditions>> weather = new HashMap<>();
    
    private LocalDateTime getForecastStartTime(int interval) {
        // forecast slots from the real server start at midnight and are every 3 hours. We want the most recent one
        // that starts before 'now'
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withHour(0).withMinute(0).withSecond(0).withNano(0);
        while (start.plusMinutes(interval).isBefore(now)) {
            start = start.plusMinutes(interval);
        }
        return start;
    }
    
    
    @Override
    public void createWeatherFromSlots(Optional<Float> latitude, Optional<Float> longitude, Optional<Integer> slotLengthMinutes, List<String> slots) {
        int interval = slotLengthMinutes.orElse(180);
        LocalDateTime sampleTime = LocalDateTime.now();
        List<Conditions> samples = new ArrayList<>();
        int windDirection = random.nextInt(360);
        float windSpeed = -1;
        
        for (String slot : slots) {
            ConditionType conditionType = ConditionType.valueOf(slot);
            Float[] windRange = ConditionsConstants.CONDITION_WIND_DEFAULTS.get(conditionType);
            int clouds = ConditionsConstants.CONDITION_CLOUD_DEFAULTS.get(conditionType);
            int humidity = ConditionsConstants.CONDITION_HUMIDITY_DEFAULTS.get(conditionType);
            int visibility = ConditionsConstants.CONDITION_VISIBILITY_DEFAULTS.get(conditionType);
            int pressure = ConditionsConstants.CONDITION_PRESSURE_DEFAULTS.get(conditionType);
            float rain = ConditionsConstants.CONDITION_RAIN_DEFAULTS.get(conditionType);
            float newWindSpeed = windRange[0] + random.nextFloat() * (windRange[1] - windRange[0]);
            if (windSpeed == -1) {
                windSpeed = newWindSpeed;
            }
            else {
                // if we already have a wind speed from the previous data point, base our next speed on this
                windSpeed = clamp(windSpeed + ((newWindSpeed - windSpeed) / 2), 0, 30);
            }
            samples.add(new Conditions(sampleTime, 
                    ConditionsConstants.CONDITION_TEMP_DEFAULTS.get(conditionType),
                    windSpeed,
                    windDirection,
                    clouds,
                    humidity,
                    visibility,
                    pressure,
                    rain,
                    new Symbol(conditionType, sampleTime.getHour() >= 6 && sampleTime.getHour() <= 18)));
            sampleTime = sampleTime.plusMinutes(interval);
        }
        Location key;
        if (latitude.isPresent() && longitude.isPresent()) {
            key = Location.create(latitude.get().intValue(), longitude.get().intValue());
        }
        else {
            key = ANY_LOCATION;
        }
        weather.put(key, samples);
        if (samples.size() > 0) {
            LOGGER.info("Created " + samples.size() + " new weather slots for any location, from " + 
                    samples.get(0).getTime().format(WeatherServiceImpl.DTF) + " to " + samples.get(samples.size() - 1).getTime().format(WeatherServiceImpl.DTF));
            StringBuilder sb = new StringBuilder();
            sb.append("Conditions [\n");
            for (Conditions sample : samples) {
                sb.append(sample.toString()).append("\n");
            }
            sb.append("]");
            LOGGER.info(sb.toString());
        }
    }
    
    @Override
    public void createWeather(Optional<Float> latitude, Optional<Float> longitude, int minutesBetweenSamples, List<Conditions> samples) {
        LocalDateTime sampleTime = LocalDateTime.now();
        for (Conditions conditionsSample : samples) {
            if (conditionsSample.getTime() == null) {
                conditionsSample.setTime(sampleTime);                
            }
            sampleTime = sampleTime.plusMinutes(minutesBetweenSamples);
        }
        Location key;
        if (latitude.isPresent() && longitude.isPresent()) {
            key = Location.create(latitude.get().intValue(), longitude.get().intValue());
        }
        else {
            key = ANY_LOCATION;
        }
        // just overwrite
        weather.put(key, samples);
    }
    
    @Override
    public WeatherData getForecast(float latitude, float longitude, int items, int minutesBetweenPoints, LocalDateTime time) {
        Location key = Location.create(latitude, longitude);
        if (!weather.containsKey(key)) {
            if (weather.containsKey(ANY_LOCATION)) {
                key = ANY_LOCATION;
            }
            else {
                createWeather(key, time, ConditionsConstants.getPrevailingConditions(latitude, longitude, time), null);
            }
        }
        // forecast data every minutesBetweenPoints, starting (by default) at a 00:00, 03:00, 06:00, etc.
        LocalDateTime firstForecastStartPointTime = getForecastStartTime(minutesBetweenPoints);
        LocalDateTime forecastStartPointTime = firstForecastStartPointTime;
        LocalDateTime forecastEndPointTime = forecastStartPointTime.plusMinutes(minutesBetweenPoints);
        
        List<Conditions> samplesForForecast = new ArrayList<>();
        List<Conditions> conditions = weather.get(key);
        // ensure there's at least 1 sample prior to the forecast start time
        Conditions firstSample = conditions.get(0);
        while (firstSample.getTime().isAfter(forecastEndPointTime)) {
            Conditions newFirstSample = firstSample.cloneForTime(firstSample.getTime().minusMinutes(minutesBetweenPoints));
            conditions.add(0, newFirstSample);
            firstSample = newFirstSample;
        }
        int itemsAdded = 0;
        samplesForForecast.add(null);
        // get a single sample between each of the forecast points.
        for (Conditions conditionsSample : conditions) {
            if (conditionsSample.getTime().isBefore(forecastEndPointTime) && !conditionsSample.getTime().isBefore(forecastStartPointTime)) {
                samplesForForecast.set(itemsAdded, conditionsSample);
            }
            else if (!conditionsSample.getTime().isBefore(forecastEndPointTime)) {
                // it's after (or at) the current forecast window endpoint, so move to the next window
                forecastEndPointTime = forecastEndPointTime.plusMinutes(minutesBetweenPoints);
                forecastStartPointTime = forecastStartPointTime.plusMinutes(minutesBetweenPoints);
                itemsAdded++;
                if (itemsAdded < items) {
                    samplesForForecast.add(itemsAdded, conditionsSample);
                }
            }
            if (itemsAdded == items) {
                break;
            }
        }
        
        WeatherData forecast = constructForecast(latitude, longitude, minutesBetweenPoints, firstForecastStartPointTime, samplesForForecast);
        if (forecast.getForecast().getTimes().size() < items) {
            LOGGER.info("padding forecast end");
            Time timeToClone = forecast.getForecast().getTimes().get(forecast.getForecast().getTimes().size() - 1);
            for (int i = forecast.getForecast().getTimes().size(); i < items + 1; i++) {
                String from = timeToClone.getTo();
                String to = LocalDateTime.parse(from, WeatherServiceImpl.DTF).plusMinutes(minutesBetweenPoints).format(WeatherServiceImpl.DTF);     
                Time cloned = timeToClone.cloneToNewPeriod(from, to);
                forecast.getForecast().getTimes().add(cloned);
                timeToClone = cloned;
            }
        }
        LOGGER.info("Got forecast location " + latitude + ", " + longitude + ": "+ forecast.toString());
        return forecast;
    }
    
    @Override
    public Current getWeather(float latitude, float longitude, LocalDateTime time) {
        Location key = Location.create(latitude, longitude);
        if (!weather.containsKey(key)) {
            if (weather.containsKey(ANY_LOCATION)) {
                key = ANY_LOCATION;
            }
            else {
                createWeather(key, time, ConditionsConstants.getPrevailingConditions(latitude, longitude, time), null);
            }
        }
        Conditions[] conditionsEitherSide = getConditionsEitherSide(key, time);
        if (conditionsEitherSide[0] == null || conditionsEitherSide[1] == null) {
            // we have no weather data so we need to create some
            createWeather(key, time, ConditionsConstants.getPrevailingConditions(latitude, longitude, time), conditionsEitherSide[1]);
            conditionsEitherSide = getConditionsEitherSide(key, time);
        }
        // now we have the conditions either side of the sample, linearly scale each value to create the current weather
        // need to know how close we are to each side here
        float proportionAfterSample0 = (float) Duration.between(conditionsEitherSide[0].getTime(), time).getSeconds() / 
                                       (float) Duration.between(conditionsEitherSide[0].getTime(), conditionsEitherSide[1].getTime()).getSeconds();
        
               
        float rain = conditionsEitherSide[0].getPrecipitation() +  ((conditionsEitherSide[1].getPrecipitation() - conditionsEitherSide[0].getPrecipitation()) * proportionAfterSample0);
        float wind = conditionsEitherSide[0].getWindSpeed() + ((conditionsEitherSide[1].getWindSpeed() - conditionsEitherSide[0].getWindSpeed()) * proportionAfterSample0);
        float temp = conditionsEitherSide[0].getTemperature() + ((conditionsEitherSide[1].getTemperature() - conditionsEitherSide[0].getTemperature()) * proportionAfterSample0);
        float pressure = conditionsEitherSide[0].getPressure() + ((conditionsEitherSide[1].getPressure() - conditionsEitherSide[0].getPressure()) * proportionAfterSample0);
        float humidity = conditionsEitherSide[0].getHumidity() + ((conditionsEitherSide[1].getHumidity() - conditionsEitherSide[0].getHumidity()) * proportionAfterSample0);
        int clouds = (int) (conditionsEitherSide[0].getClouds() + ((conditionsEitherSide[1].getClouds() - conditionsEitherSide[0].getClouds()) * proportionAfterSample0));
        int visibility = (int) (conditionsEitherSide[0].getVisibility() + ((conditionsEitherSide[1].getVisibility() - conditionsEitherSide[0].getVisibility()) * proportionAfterSample0));       

        Current sample = new Current(
                new City(new Coord(latitude, longitude), new Sun(getSunrise(time), getSunset(time))),
                new CurrentPrecipitation(convertRainNumberToMMIn3Hours(rain)),
                new Wind(new Speed(wind), new Direction(conditionsEitherSide[1].getWindDirection())),
                new CurrentTemperature("kelvin", temp, temp, temp),
                new CurrentPressure("hPa", pressure),
                new Humidity((int) humidity, "%"),
                new CurrentClouds(clouds), 
                new Visibility(visibility),
                Weather.generate(rain, visibility, clouds, conditionsEitherSide[1].getTime()));
        LOGGER.info("got current weather for location " + latitude + "," + longitude + ": " + sample.toString());
        return sample;
    }
    
    private WeatherData constructForecast(float latitude, float longitude, int minutesBetweenSamples, LocalDateTime forecastStartTime, List<Conditions> conditions) {
        List<Time> times = new ArrayList<>();
        int index = 0;
        for (Conditions conditionsSample : conditions) {
            LocalDateTime thisStartTime = forecastStartTime.plusMinutes(minutesBetweenSamples * index);
            LocalDateTime thisEndTime =  thisStartTime.plusMinutes(minutesBetweenSamples);
            times.add(new Time(thisStartTime.format(WeatherServiceImpl.DTF),
                               thisEndTime.format(WeatherServiceImpl.DTF),
                               new org.belowski.weather.model.forecast.ForecastPrecipitation(convertRainNumberToMMIn3Hours(conditionsSample.getPrecipitation())),
                               new WindDirection(conditionsSample.getWindDirection()), 
                               new WindSpeed(conditionsSample.getWindSpeed()), 
                               new ForecastTemperature("kelvin", conditionsSample.getTemperature(), conditionsSample.getTemperature(), conditionsSample.getTemperature()),
                               new ForecastPressure("hPa", conditionsSample.getPressure()),
                               new Humidity(conditionsSample.getHumidity(), "%"),
                               new org.belowski.weather.model.forecast.ForecastClouds(conditionsSample.getClouds(), "%"),
                               conditionsSample.getPrecipitation(),
                               conditionsSample.getVisibility(),
                               thisEndTime.getHour() >= 6 && thisEndTime.getHour() <= 18));
            index++;
        }
        return new WeatherData(new Sun(getSunrise(forecastStartTime), getSunset(forecastStartTime)), 
                new LocationWrapper(new org.belowski.weather.model.forecast.Location(latitude, longitude)), new Forecast(times));
    }
    
    private String getSunrise(LocalDateTime time) {
        return time.withHour(6).format(WeatherServiceImpl.DTF);
    }
    
    private String getSunset(LocalDateTime time) {
        return time.withHour(20).format(WeatherServiceImpl.DTF);
    }
    
    // internal rain amount is 0 - 1. Need to convert this to rain in mm for 3 hours. 30mm in 3hours is max    
    private float convertRainNumberToMMIn3Hours(float rainNumber) {
        // rain number is 0 - 1.
        if (rainNumber < 0.5) {
            return 0;
        }
        return (rainNumber - 0.5f) * 60;
    }

    private void createWeather(Location location, LocalDateTime start, ConditionsConstants.PrevailingConditions prevailingConditions, Conditions initialConditions) {
        LOGGER.info("Creating weather with prevailing conditions " + prevailingConditions);
        List<Conditions> generatedConditions = new ArrayList<>();
        // create 24 hours (?) of weather from start - 1 hour
        LocalDateTime sampleTime = start.minusHours(1);
        // start with our first sample. If we have initialConditions, use them without altering them
        Conditions previousConditions = null;
        Conditions previousPreviousConditions = null;
        if (initialConditions != null) {
            previousConditions = initialConditions.cloneForTime(start);
        }
        LocalDateTime lastConditionsDirectionChange = sampleTime;
        ChangeType changeType = random.nextInt(2) == 0 ? ChangeType.IMPROVING : ChangeType.WORSENING;
        for (int i=0; i<hoursToCreate * samplesPerHour; i++) {
            sampleTime = sampleTime.plusMinutes(10);
            Conditions theseConditions = createNewConditions(prevailingConditions, sampleTime, previousPreviousConditions, previousConditions, changeType);
            generatedConditions.add(theseConditions);
            previousPreviousConditions = previousConditions;
            previousConditions = theseConditions;
            long secondsSinceDirectionChange = Duration.between(lastConditionsDirectionChange, sampleTime).getSeconds();
            if (secondsSinceDirectionChange > minTimeBeforeChangingWeatherDirection && secondsSinceDirectionChange < maxTimeBeforeChangingWeatherDirection &&
                    random.nextInt(10) > 6) {
                // change the direction
                changeType = changeType == ChangeType.IMPROVING ? ChangeType.WORSENING : ChangeType.IMPROVING;
                lastConditionsDirectionChange = sampleTime;
            }
        }        
        weather.put(location, generatedConditions);
    }
    
    private Conditions createNewConditions(ConditionsConstants.PrevailingConditions prevailingConditions,
            LocalDateTime time, Conditions previousPreviousConditions, Conditions previousConditions, ChangeType changeType) {
        
        // scale the rain likelihood - make it very rare for dry places, twice as common for wet places
        float rainLikelihoodScale;
        float tempAdjustment;
        switch (prevailingConditions) {
            case DESERT:
                rainLikelihoodScale = 0.05f;
                tempAdjustment = 10f;
                break;
            case DRY:
                rainLikelihoodScale = 0.5f;
                tempAdjustment = 2f;
                break;
            case WET:
                rainLikelihoodScale = 2f;
                tempAdjustment = -5f;
                break;
            default:
                rainLikelihoodScale = 1f;
                tempAdjustment = 1f;
                break;
        }
        if (previousConditions == null) {
            // need to guess some start conditions
            // link rain and humidity here
            float rain = random.nextFloat() * rainLikelihoodScale > 0.7 ? random.nextFloat() * maxRain : 0;
            return new Conditions(time, 
                    minTemp + (random.nextFloat() * (maxTemp - minTemp)) + tempAdjustment,
                    minWindSpeed + (random.nextFloat() * (maxWindSpeed - minWindSpeed)),
                    random.nextInt(360),
                    rainToClouds(rain),
                    rainToHumidity(rain), 
                    (int) (300 + ((1f - rain) * 10000f)),
                    (int) (minPressure + (random.nextFloat() * (maxPressure - minPressure))),
                    rain,
                    null);
        }
        else {
            long secondsSincePrevious = Duration.between(previousConditions.getTime(), time).getSeconds();
            float deltaRain = 0;
            float deltaWind = 0;
            float deltaTemp = 0;
            float deltaPressure = 0;
            if (previousPreviousConditions != null) {
                // continue changing in a (mostly) linear way - use the same delta, +/- 5%
                float changeScale = (changeType == ChangeType.IMPROVING ? -1 : 1) + ((random.nextFloat() - 0.5f) / 10f);
                deltaRain = Math.abs(previousConditions.getPrecipitation() - previousPreviousConditions.getPrecipitation()) * changeScale;
                deltaWind =  Math.abs(previousConditions.getWindSpeed() - previousPreviousConditions.getWindSpeed()) * changeScale;
                deltaTemp = Math.abs(previousConditions.getTemperature() - previousPreviousConditions.getTemperature()) * changeScale;
                deltaPressure = Math.abs(previousConditions.getPressure() - previousPreviousConditions.getPressure()) * changeScale;
            }
            
            // for any deltas which are zero, allow a small change to happen randomly
            if (deltaRain == 0 && random.nextFloat() > 0.7) {
                deltaRain = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1f : rainLikelihoodScale) * (float)secondsSincePrevious / (float)secondsForFullRainTransition;
            }
            if (deltaWind == 0 && random.nextFloat() > 0.7) {
                deltaWind = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1f : 1f) * (float)secondsSincePrevious / (float)secondsForFullWindTransition;
            }
            if (deltaTemp == 0 && random.nextFloat() > 0.7) {
                deltaTemp = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1f : 1f) * (float)secondsSincePrevious / (float)secondsForFullTempTransition;
            }
            if (deltaPressure == 0 && random.nextFloat() > 0.7) {
                deltaPressure = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1f : 1f) * (float)secondsSincePrevious / (float)secondsForFullRainTransition;
            }
            
            // make the transition from dry to rain less likely
            float newRainAmount = clamp(previousConditions.getPrecipitation() + deltaRain, 0, maxRain);
            if (previousConditions.getPrecipitation() == 0 && random.nextFloat() > 0.2) {
                newRainAmount = 0;
            }
            return new Conditions(time, 
                    clamp(previousConditions.getTemperature() + deltaTemp, minTemp, maxTemp),
                    clamp(previousConditions.getWindSpeed() + deltaWind, minWindSpeed, maxWindSpeed),
                    previousConditions.getWindDirection(),
                    rainToClouds(newRainAmount),
                    rainToHumidity(newRainAmount),
                    (int) (300 + ((1f - newRainAmount) * 10000f)),
                    (int) clamp(previousConditions.getPressure() + deltaPressure, minPressure, maxPressure),
                    newRainAmount,
                    null);
        }
    }
    
    private float clamp(float val, float min, float max) {
        return val > max ? max : val < min ? min : val;
    }
    
    private int rainToHumidity(float rain) {
        if (rain > 0.6) {
            return 100;
        }
        else if (rain == 0) {
            return (int) (random.nextFloat() * 20f);
        }
        else {
            return (int) (rain * 100);
        }
    }
    
    private int rainToClouds(float rain) {       
        if (rain == 0) {
            return (int) (random.nextFloat() * 40f);
        }
        else {
            return (int) (rain * 100);
        }
    }
    
    private Conditions[] getConditionsEitherSide(Location key, LocalDateTime time) {
        Conditions[] conditionsEitherSide = new Conditions[2];
        if (weather.containsKey(key)) {
            for (Conditions conditions : weather.get(key)) {
                if (conditions.getTime().isBefore(time)) {
                    conditionsEitherSide[0] = conditions;
                }
                else if (!conditions.getTime().isBefore(time)) {
                    conditionsEitherSide[1] = conditions;
                    break;
                }
            }
        }
        return conditionsEitherSide;
    }
}
