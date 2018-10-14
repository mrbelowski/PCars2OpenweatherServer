package org.belowski.weather.model;

import java.time.Month;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ConditionsConstants {
    
    public enum ConditionType {
        THUNDERSTORM,
        LIGHT_DRIZZLE, DRIZZLE, HEAVY_DRIZZLE,
        LIGHT_RAIN, RAIN, HEAVY_RAIN, VERY_HEAVY_RAIN,
        HAZE, MIST, FOG, 
        CLEAR, SCATTERED_CLOUD, CLOUD, THICK_CLOUD, OVERCAST
    }
    
    public enum PrevailingConditions {DESERT, DRY, TEMPERATE, WET};
    
    public enum Season {WINTER, SPRING, SUMMER, AUTUMN}
    
    // lat / lon pairs for the centre of regions considered 'dry'
    private static float[][] dryRegions = {new float[] {37, -120}, new float[] {30, -102}, new float[] {23, 37}, new float[] {23, 47}};    // dry US regions + the Gulf
    // lat / lon pairs for the centre of regions considered 'wet'
    private static float[][] wetRegions = {new float[] {55, 7}, new float[] {37, 147}};    // just UK and Japan for now

    public static final Map<ConditionType, Integer> CONDITION_IDS = new HashMap<>();
    public static final Map<ConditionType, Float> CONDITION_TEMP_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Integer> CONDITION_HUMIDITY_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Integer> CONDITION_VISIBILITY_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Integer> CONDITION_PRESSURE_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Float[]> CONDITION_WIND_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Float> CONDITION_RAIN_DEFAULTS = new HashMap<>();
    public static final Map<ConditionType, Integer> CONDITION_CLOUD_DEFAULTS = new HashMap<>();
    
    private static List<Month> northernHemisphereWinter = Arrays.asList(new Month[] {Month.DECEMBER, Month.JANUARY, Month.FEBRUARY});
    private static List<Month> northernHemisphereSpring = Arrays.asList(new Month[] {Month.MARCH, Month.APRIL, Month.MAY});
    private static List<Month> northernHemisphereSummer = Arrays.asList(new Month[] {Month.JUNE, Month.JULY, Month.AUGUST});
    private static List<Month> northernHemisphereAutumn = Arrays.asList(new Month[] {Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER});
    
    static {
        CONDITION_IDS.put(ConditionType.THUNDERSTORM, 202);
        CONDITION_IDS.put(ConditionType.LIGHT_DRIZZLE, 300);
        CONDITION_IDS.put(ConditionType.DRIZZLE, 301);
        CONDITION_IDS.put(ConditionType.HEAVY_DRIZZLE, 302);
        CONDITION_IDS.put(ConditionType.LIGHT_RAIN, 500);
        CONDITION_IDS.put(ConditionType.RAIN, 501);
        CONDITION_IDS.put(ConditionType.HEAVY_RAIN, 502);
        CONDITION_IDS.put(ConditionType.VERY_HEAVY_RAIN, 503);
        CONDITION_IDS.put(ConditionType.HAZE, 721);
        CONDITION_IDS.put(ConditionType.MIST, 701);
        CONDITION_IDS.put(ConditionType.FOG, 741);
        CONDITION_IDS.put(ConditionType.CLEAR, 800);
        CONDITION_IDS.put(ConditionType.SCATTERED_CLOUD, 801);
        CONDITION_IDS.put(ConditionType.CLOUD, 802);
        CONDITION_IDS.put(ConditionType.THICK_CLOUD, 803);
        CONDITION_IDS.put(ConditionType.OVERCAST, 804);

        CONDITION_TEMP_DEFAULTS.put(ConditionType.THUNDERSTORM, 10f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 25f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.DRIZZLE, 23f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 20f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.LIGHT_RAIN, 18f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.RAIN, 15f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.HEAVY_RAIN, 13f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 12f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.HAZE, 20f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.MIST, 10f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.FOG, 10f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.CLEAR, 30f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 26f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.CLOUD, 22f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.THICK_CLOUD, 18f);
        CONDITION_TEMP_DEFAULTS.put(ConditionType.OVERCAST, 12f);
        
        CONDITION_RAIN_DEFAULTS.put(ConditionType.THUNDERSTORM, 1f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 0.05f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.DRIZZLE, 0.1f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 0.3f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.LIGHT_RAIN, 0.4f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.RAIN, 0.5f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.HEAVY_RAIN, 0.7f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 0.9f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.HAZE, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.MIST, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.FOG, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.CLEAR, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.CLOUD, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.THICK_CLOUD, 0f);
        CONDITION_RAIN_DEFAULTS.put(ConditionType.OVERCAST, 0f);
        
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.THUNDERSTORM, 100);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 40);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.DRIZZLE, 50);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 60);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.LIGHT_RAIN, 80);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.RAIN, 100);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.HEAVY_RAIN, 100);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 100);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.HAZE, 20);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.MIST, 50);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.FOG, 100);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.CLEAR, 0);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 10);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.CLOUD, 20);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.THICK_CLOUD, 50);
        CONDITION_CLOUD_DEFAULTS.put(ConditionType.OVERCAST, 100);
        
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.THUNDERSTORM, 970);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 995);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.DRIZZLE, 993);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 990);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.LIGHT_RAIN, 987);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.RAIN, 985);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.HEAVY_RAIN, 980);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 975);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.HAZE, 1005);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.MIST, 1000);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.FOG, 1000);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.CLEAR, 1020);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 1010);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.CLOUD, 1005);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.THICK_CLOUD, 990);
        CONDITION_PRESSURE_DEFAULTS.put(ConditionType.OVERCAST, 990);
        
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.THUNDERSTORM, 100);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 50);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.DRIZZLE, 60);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 70);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.LIGHT_RAIN, 80);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.RAIN, 100);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.HEAVY_RAIN, 100);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 100);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.HAZE, 50);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.MIST, 70);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.FOG, 100);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.CLEAR, 0);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 10);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.CLOUD, 40);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.THICK_CLOUD, 70);
        CONDITION_HUMIDITY_DEFAULTS.put(ConditionType.OVERCAST, 80);
        
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.THUNDERSTORM, 500);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, 6000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.DRIZZLE, 4000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, 3500);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.LIGHT_RAIN, 3000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.RAIN, 2000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.HEAVY_RAIN, 1000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, 500);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.HAZE, 2000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.MIST, 1000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.FOG, 300);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.CLEAR, 20000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, 10000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.CLOUD, 8000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.THICK_CLOUD, 6000);
        CONDITION_VISIBILITY_DEFAULTS.put(ConditionType.OVERCAST, 6000);

        CONDITION_WIND_DEFAULTS.put(ConditionType.THUNDERSTORM, new Float[] {15f, 30f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.LIGHT_DRIZZLE, new Float[] {0f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.DRIZZLE, new Float[] {0f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.HEAVY_DRIZZLE, new Float[] {5f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.LIGHT_RAIN, new Float[] {5f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.RAIN, new Float[] {5f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.HEAVY_RAIN, new Float[] {5f, 15f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.VERY_HEAVY_RAIN, new Float[] {5f, 20f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.HAZE, new Float[] {0f, 5f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.MIST, new Float[] {0f, 2f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.FOG, new Float[] {0f, 2f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.CLEAR, new Float[] {0f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.SCATTERED_CLOUD, new Float[] {0f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.CLOUD, new Float[] {5f, 10f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.THICK_CLOUD, new Float[] {5f, 15f});
        CONDITION_WIND_DEFAULTS.put(ConditionType.OVERCAST, new Float[] {5f, 15f});
    }
    
    private static final Random random = new Random();
    
    public static PrevailingConditions getPrevailingConditions(float latitude, float longitude, LocalDateTime time) {
        Season season = getSeason(latitude, time);
        for (float[] dryPair : dryRegions) {
            if (Math.abs(latitude - dryPair[0]) < 8 && Math.abs(longitude - dryPair[1]) < 8) {
                return season == Season.WINTER ? PrevailingConditions.DRY : PrevailingConditions.DESERT;
            }
        }
        for (float[] wetPair : wetRegions) {
            if (Math.abs(latitude - wetPair[0]) < 8 && Math.abs(longitude - wetPair[1]) < 8) {
                return season == Season.SUMMER ? PrevailingConditions.TEMPERATE : PrevailingConditions.WET;
            }
        }
        return season == Season.SUMMER ? PrevailingConditions.DRY : PrevailingConditions.TEMPERATE;
    }
    
    public static Season getSeason(float latitude, LocalDateTime time) {
        if (latitude > 0) {
            if (northernHemisphereWinter.contains(time.getMonth())) {
                return Season.WINTER;
            }
            else if (northernHemisphereSpring.contains(time.getMonth())) {
                return Season.SPRING;
            }
            else if (northernHemisphereSummer.contains(time.getMonth())) {
                return Season.SUMMER;
            }
            else {
                return Season.AUTUMN;
            }
        }
        else {
            if (northernHemisphereSummer.contains(time.getMonth())) {
                return Season.WINTER;
            }
            else if (northernHemisphereAutumn.contains(time.getMonth())) {
                return Season.SPRING;
            }
            else if (northernHemisphereWinter.contains(time.getMonth())) {
                return Season.SUMMER;
            }
            else {
                return Season.AUTUMN;
            }
        }
    }
    
    public static ConditionType getConditionType(float rainAmount, int visibility, int clouds) {
        if (rainAmount == 1) {
            return random.nextInt(10) < 9 ? ConditionType.VERY_HEAVY_RAIN : ConditionType.THUNDERSTORM;
        }
        else if (rainAmount >= 0.7) {
            return ConditionType.HEAVY_RAIN;
        }
        else if (rainAmount >= 0.5) {
            return ConditionType.RAIN;
        }
        else if (rainAmount >= 0.4) {
            return ConditionType.LIGHT_RAIN;
        }
        else if (rainAmount >= 0.3) {
            return ConditionType.HEAVY_DRIZZLE;
        }
        else if (rainAmount >= 0.2) {
            return ConditionType.DRIZZLE;
        }
        else if (rainAmount >= 0.01) {
            return ConditionType.LIGHT_DRIZZLE;
        }
        else if (visibility <= 400) {
            return ConditionType.FOG;
        }
        else if (visibility <= 1000) {
            return ConditionType.MIST;
        }        
        else if (visibility < 4000) {
            return ConditionType.HAZE;
        }
        else if (clouds >= 80) {
            return ConditionType.OVERCAST;
        }
        else if (clouds >= 60) {
            return ConditionType.THICK_CLOUD;
        }
        else if (clouds >= 40) {
            return ConditionType.CLOUD;
        }
        else if (clouds >= 20) {
            return ConditionType.SCATTERED_CLOUD;
        }
        else {
            return ConditionType.CLEAR;
        }
    }
}
