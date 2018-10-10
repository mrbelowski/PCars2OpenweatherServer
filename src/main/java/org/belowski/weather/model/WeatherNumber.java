package org.belowski.weather.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WeatherNumber {
    
    public enum ConditionType {
        THUNDERSTORM,
        LIGHT_DRIZZLE, DRIZZLE, HEAVY_DRIZZLE,
        LIGHT_RAIN, RAIN, HEAVY_RAIN, VERY_HEAVY_RAIN,
        HAZE, MIST, FOG, 
        CLEAR, SCATTERED_CLOUD, CLOUD, THICK_CLOUD, OVERCAST
    }

    public static final Map<ConditionType, Integer> CONDITION_IDS = new HashMap<>();
    public static final Map<ConditionType, Float> CONDITION_TEMP_DEFAULTS = new HashMap<>();
    
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
    }
    
    static {
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
    }
    
    private static final Random random = new Random();
    
    public static ConditionType getConditionType(float rainAmount, int visibility, int clouds) {
        if (rainAmount == 1) {
            return random.nextInt(10) < 9 ? ConditionType.VERY_HEAVY_RAIN : ConditionType.THUNDERSTORM;
        }
        else if (rainAmount > 0.7) {
            return ConditionType.HEAVY_RAIN;
        }
        else if (rainAmount > 0.5) {
            return ConditionType.RAIN;
        }
        else if (rainAmount > 0.4) {
            return ConditionType.LIGHT_RAIN;
        }
        else if (rainAmount > 0.3) {
            return ConditionType.HEAVY_DRIZZLE;
        }
        else if (rainAmount > 0.2) {
            return ConditionType.DRIZZLE;
        }
        else if (rainAmount > 0.01) {
            return ConditionType.LIGHT_DRIZZLE;
        }
        else if (clouds > 80) {
            return ConditionType.OVERCAST;
        }
        else if (clouds > 60) {
            return ConditionType.THICK_CLOUD;
        }
        else if (clouds > 40) {
            return ConditionType.CLOUD;
        }
        else if (clouds > 20) {
            return ConditionType.SCATTERED_CLOUD;
        }
        else if (visibility < 300) {
            return ConditionType.FOG;
        }
        else if (visibility < 1000) {
            return ConditionType.MIST;
        }
        else if (visibility < 4000) {
            return ConditionType.HAZE;
        }
        else {
            return ConditionType.CLEAR;
        }
    }
}
