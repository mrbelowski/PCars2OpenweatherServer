package org.belowski.weather.repository;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Sun;
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
    
    private static final Logger LOGGER = Logger.getLogger(WeatherServiceImpl.class.getName());
    
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
    
    @Override
    public void createWeather(float latitude, float longitude, int minutesBetweenSamples, List<Conditions> samples) {
        ZonedDateTime sampleTime = ZonedDateTime.now(ZoneOffset.UTC);
        for (Conditions conditionsSample : samples) {
            if (conditionsSample.getTime() == null) {
                conditionsSample.setTime(sampleTime);                
            }
            sampleTime = sampleTime.plusMinutes(minutesBetweenSamples);
        }
        
        Location key = new Location((int) latitude, (int) longitude);
        // just overwrite
        weather.put(key, samples);
    }
    
    @Override
    public WeatherData getForecast(float latitude, float longitude, int items, int minutesBetweenPoints, ZonedDateTime time) {
        Location key = new Location((int) latitude, (int) longitude);      
        if (!weather.containsKey(key)) {
            createWeather(key, time, null);
        }
        // forecast data every minutesBetweenPoints
        ZonedDateTime forecastTime = time.minusMinutes(minutesBetweenPoints);  
        List<Conditions> samplesForForecast = new ArrayList<>();
        List<Conditions> conditions = weather.get(key);
        int itemsAdded = 0;
        for (Conditions conditionsSample : conditions) {
            if (conditionsSample.getTime().isAfter(forecastTime)) {
                samplesForForecast.add(conditionsSample);
                forecastTime = forecastTime.plusMinutes(minutesBetweenPoints);
                itemsAdded++;
            }
            if (itemsAdded == items) {
                break;
            }
        }
        WeatherData forecast = constructForecast(latitude, longitude, minutesBetweenPoints, time, samplesForForecast);
        LOGGER.info("Got forecast location " + latitude + "-" + longitude + ": "+ forecast.toString());
        return forecast;
    }
    
    @Override
    public Current getWeather(float latitude, float longitude, ZonedDateTime time) {
        Location key = new Location((int) latitude, (int) longitude);
        Conditions[] conditionsEitherSide = getConditionsEitherSide(key, time);
        if (conditionsEitherSide[0] == null) {
            // we have no weather data so we need to create some
            createWeather(key, time, conditionsEitherSide[1]);
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
                Weather.generate(rain, visibility, clouds));
        LOGGER.info("got current weather for location " + latitude + "-" + longitude + ": " + sample.toString());
        return sample;
    }
    
    private WeatherData constructForecast(float latitude, float longitude, int minutesBetweenSamples, ZonedDateTime time, List<Conditions> conditions) {
        List<Time> times = new ArrayList<>();
        for (Conditions conditionsSample : conditions) {
            times.add(new Time(conditionsSample.getTime().withSecond(0).format(WeatherServiceImpl.DTF),
                               conditionsSample.getTime().withSecond(0).plusMinutes(minutesBetweenSamples).format(WeatherServiceImpl.DTF),
                               new org.belowski.weather.model.forecast.ForecastPrecipitation(convertRainNumberToMMIn3Hours(conditionsSample.getPrecipitation())),
                               new WindDirection(conditionsSample.getWindDirection()), 
                               new WindSpeed(conditionsSample.getWindSpeed()), 
                               new ForecastTemperature("kelvin", conditionsSample.getTemperature(), conditionsSample.getTemperature(), conditionsSample.getTemperature()),
                               new ForecastPressure("hPa", conditionsSample.getPressure()),
                               new Humidity(conditionsSample.getHumidity(), "%"),
                               new org.belowski.weather.model.forecast.ForecastClouds(conditionsSample.getClouds(), "%"),
                               conditionsSample.getPrecipitation(),
                               conditionsSample.getVisibility()));
        }
        return new WeatherData(new Sun(getSunrise(time), getSunset(time)), 
                new LocationWrapper(new org.belowski.weather.model.forecast.Location(latitude, longitude)), new Forecast(times));
    }
    
    private String getSunrise(ZonedDateTime time) {
        return time.withHour(6).format(WeatherServiceImpl.DTF);
    }
    
    private String getSunset(ZonedDateTime time) {
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

    private void createWeather(Location location, ZonedDateTime start, Conditions initialConditions) {
        List<Conditions> generatedConditions = new ArrayList<>();
        // create 24 hours (?) of weather from start - 1 hour
        ZonedDateTime sampleTime = start.minusHours(1);
        // start with our first sample. If we have initialConditions, use them without altering them
        Conditions previousConditions = null;
        Conditions previousPreviousConditions = null;
        if (initialConditions != null) {
            previousConditions = initialConditions.cloneForTime(start);
        }
        ZonedDateTime lastConditionsDirectionChange = sampleTime;
        ChangeType changeType = random.nextInt(2) == 0 ? ChangeType.IMPROVING : ChangeType.WORSENING;
        for (int i=0; i<hoursToCreate * samplesPerHour; i++) {
            sampleTime = sampleTime.plusMinutes(10);
            Conditions theseConditions = createNewConditions(sampleTime, previousPreviousConditions, previousConditions, changeType);
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
    
    private Conditions createNewConditions(ZonedDateTime time, Conditions previousPreviousConditions, Conditions previousConditions, ChangeType changeType) {
        if (previousConditions == null) {
            // need to guess some start conditions
            // link rain and humidity here
            float rain = random.nextFloat() > 0.8 ? random.nextFloat() * maxRain : 0;
            return new Conditions(time, 
                    minTemp + (random.nextFloat() * (maxTemp - minTemp)),
                    rain,
                    minPressure + (random.nextFloat() * (maxPressure - minPressure)),
                    rainToHumidity(rain), 
                    rainToClouds(rain),
                    minWindSpeed + (random.nextFloat() * (maxWindSpeed - minWindSpeed)),
                    random.nextInt(360),
                    (int) (300 + ((1f - rain) * 10000f)));
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
                deltaRain = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1f : 1f) * (float)secondsSincePrevious / (float)secondsForFullRainTransition;
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
                    newRainAmount,
                    clamp(previousConditions.getPressure() + deltaPressure, minPressure, maxPressure), 
                    rainToHumidity(newRainAmount), 
                    rainToClouds(newRainAmount),
                    clamp(previousConditions.getWindSpeed() + deltaWind, minWindSpeed, maxWindSpeed),
                    previousConditions.getWindDirection(),
                    (int) (300 + ((1f - newRainAmount) * 10000f)));
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
    
    private Conditions[] getConditionsEitherSide(Location key, ZonedDateTime time) {
        Conditions[] conditionsEitherSide = new Conditions[2];
        if (weather.containsKey(key)) {
            for (Conditions conditions : weather.get(key)) {
                if (conditions.getTime().isBefore(time)) {
                    conditionsEitherSide[0] = conditions;
                }
                else if (conditions.getTime().isAfter(time)) {
                    conditionsEitherSide[1] = conditions;
                    break;
                }
            }
        }
        return conditionsEitherSide;
    }
}
