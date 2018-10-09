package org.belowski.weather.repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.belowski.weather.model.Humidity;
import org.belowski.weather.model.Pressure;
import org.belowski.weather.model.Temperature;
import org.belowski.weather.model.current.Clouds;
import org.belowski.weather.model.current.Current;
import org.belowski.weather.model.current.Direction;
import org.belowski.weather.model.current.Precipitation;
import org.belowski.weather.model.current.Speed;
import org.belowski.weather.model.current.Visibility;
import org.belowski.weather.model.current.Wind;
import org.belowski.weather.model.forecast.Forecast;
import org.belowski.weather.model.forecast.Sun;
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
    
    private enum ChangeType { IMPROVING, WORSENING };
    
    private static final Random random = new Random();
    
    private int hoursToCreate = 24;
    private int samplesPerHour = 6;
    private int secondsForFullRainTransition = 30 * 60;  // rain changes take this many seconds to go from 0 (dry) to 1 (monsoon)
    private int secondsForFullWindTransition = 120 * 60; // wind changes take this many seconds to change from min to max
    private int secondsForFullTempTransition = 120 * 60;   // temp changes take this many seconds to change from min to max
    
    private float minTemp = 10f;
    private float maxTemp = 20f;
    private float maxRain = 1;
    private float minWindSpeed = 0f;
    private float maxWindSpeed = 20f;
    private float minPressure = 980f;
    private float maxPressure = 1020f;
    private int minTimeBeforeChangingWeatherDirection = 10 * 60;    // don't change from improving to worsening before 10 minutes
    private int maxTimeBeforeChangingWeatherDirection = 60 * 120;    // don't allow weather to continue worsening for > 2 hours
    
    private Map<Location, List<Conditions>> weather = new HashMap<>();
    
    @Override
    public void createWeather(float latitude, float longitude, int minutesBetweenSamples, List<Conditions> samples) {
        LocalDateTime sampleTime = LocalDateTime.now();
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
    public WeatherData getForecast(float latitude, float longitude, LocalDateTime time) {
        Location key = new Location((int) latitude, (int) longitude);      
        if (!weather.containsKey(key)) {
            createWeather(key, time, null);
        }
        // forecast data every 3 hours
        LocalDateTime forecastTime = time.plusHours(3);
        List<Conditions> samplesForForecast = new ArrayList<>();
        List<Conditions> conditions = weather.get(key);
        for (Conditions conditionsSample : conditions) {
            if (conditionsSample.getTime().isAfter(forecastTime)) {
                samplesForForecast.add(conditionsSample);
                forecastTime = forecastTime.plusHours(3);
            }
        }
        return constructForecast(conditions);
    }
    
    @Override
    public Current getWeather(float latitude, float longitude, LocalDateTime time) {
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
        float clouds = conditionsEitherSide[0].getClouds() + ((conditionsEitherSide[1].getClouds() - conditionsEitherSide[0].getClouds()) * proportionAfterSample0);
        float visibility = conditionsEitherSide[0].getVisibility() + ((conditionsEitherSide[1].getVisibility() - conditionsEitherSide[0].getVisibility()) * proportionAfterSample0);

        return new Current(
                new Precipitation(convertRainNumberToMMIn3Hours(rain)),
                new Wind(new Speed(wind), new Direction(conditionsEitherSide[1].getWindDirection())),
                new Temperature("kelvin", temp, temp, temp),
                new Pressure("hPa", pressure),
                new Humidity((int) humidity, "%"),
                new Clouds((int) clouds), 
                new Visibility((int) visibility));
    }
    
    private WeatherData constructForecast(List<Conditions> conditions) {
        List<Time> times = new ArrayList<>();
        for (Conditions conditionsSample : conditions) {
            times.add(new Time(conditionsSample.getTime().format(WeatherServiceImpl.DTF),
                               conditionsSample.getTime().plusHours(3).format(WeatherServiceImpl.DTF),
                               new org.belowski.weather.model.forecast.Precipitation(convertRainNumberToMMIn3Hours(conditionsSample.getPrecipitation())),
                               new WindDirection(conditionsSample.getWindDirection()), 
                               new WindSpeed(conditionsSample.getWindSpeed()), 
                               new Temperature("kelvin", conditionsSample.getTemperature(), conditionsSample.getTemperature(), conditionsSample.getTemperature()),
                               new Pressure("hPa", conditionsSample.getPressure()),
                               new Humidity(conditionsSample.getHumidity(), "%"),
                               new org.belowski.weather.model.forecast.Clouds(conditionsSample.getClouds(), "%")));
        }
        return new WeatherData(new Sun("2018-10-07T05:03:58", "2018-10-07T16:19:16"), new Forecast(times));
    }
    
    // internal rain amount is 0 - 1. Need to convert this to rain in mm for 3 hours. 30mm in 3hours is max    
    private float convertRainNumberToMMIn3Hours(float rainNumber) {
        // rain number is 0 - 1.
        if (rainNumber < 0.5) {
            return 0;
        }
        return (rainNumber - 0.5f) * 60;
    }

    private void createWeather(Location location, LocalDateTime start, Conditions initialConditions) {
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
    
    private Conditions createNewConditions(LocalDateTime time, Conditions previousPreviousConditions, Conditions previousConditions, ChangeType changeType) {
        if (previousConditions == null) {
            // need to guess some start conditions
            // link rain and humidity here
            float rain = random.nextFloat() * maxRain;
            return new Conditions(time, 
                    minTemp + (random.nextFloat() * (maxTemp - minTemp)),
                    rain,
                    minPressure + (random.nextFloat() * (maxPressure - minPressure)),
                    rainToHumidity(rain), 
                    rainToClouds(rain),
                    minWindSpeed + (random.nextFloat() * (maxWindSpeed - minWindSpeed)),
                    random.nextInt(360),
                    (int) ((1-rain) * 20000));
        }
        else {
            long secondsSincePrevious = Duration.between(previousConditions.getTime(), time).getSeconds();
            float deltaRain = 0;
            float deltaWind = 0;
            float deltaTemp = 0;
            float deltaPressure = 0;
            if (previousPreviousConditions != null) {
                // continue changing in a (mostly) linear way - use the same delta, +/- 10%
                float changeScale = (changeType == ChangeType.IMPROVING ? -1 : 1) + ((random.nextFloat() - 0.5f) / 5);
                deltaRain = Math.abs(previousConditions.getPrecipitation() - previousPreviousConditions.getPrecipitation()) * changeScale;
                deltaWind =  Math.abs(previousConditions.getWindSpeed() - previousPreviousConditions.getWindSpeed()) * changeScale;
                deltaTemp = Math.abs(previousConditions.getTemperature() - previousPreviousConditions.getTemperature()) * changeScale;
                deltaPressure = Math.abs(previousConditions.getPressure() - previousPreviousConditions.getPressure()) * changeScale;
            }
            
            // for any deltas which are zero, allow a small change to happen randomly
            if (deltaRain == 0 && random.nextFloat() > 0.7) {
                deltaRain = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1 : 1) * secondsSincePrevious / secondsForFullRainTransition;
            }
            if (deltaWind == 0 && random.nextFloat() > 0.7) {
                deltaWind = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1 : 1) * secondsSincePrevious / secondsForFullWindTransition;
            }
            if (deltaTemp == 0 && random.nextFloat() > 0.7) {
                deltaTemp = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1 : 1) * secondsSincePrevious / secondsForFullTempTransition;
            }
            if (deltaPressure == 0 && random.nextFloat() > 0.7) {
                deltaPressure = random.nextFloat() * (changeType == ChangeType.IMPROVING ? -1 : 1) * secondsSincePrevious / secondsForFullRainTransition;
            }
            
            float newRainAmount = clamp(previousConditions.getPrecipitation() + deltaRain, 0, maxRain);
            return new Conditions(time, 
                    clamp(previousConditions.getTemperature() + deltaTemp, minTemp, maxTemp), 
                    newRainAmount,
                    clamp(previousConditions.getPressure() + deltaPressure, minPressure, maxPressure), 
                    rainToHumidity(newRainAmount), 
                    rainToClouds(newRainAmount),
                    clamp(previousConditions.getWindSpeed() + deltaWind, minWindSpeed, maxWindSpeed),
                    previousConditions.getWindDirection(),
                    (int) ((1-newRainAmount) * 20000));
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
                else if (conditions.getTime().isAfter(time)) {
                    conditionsEitherSide[1] = conditions;
                    break;
                }
            }
        }
        return conditionsEitherSide;
    }
}
