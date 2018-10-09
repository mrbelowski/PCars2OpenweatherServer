package org.belowski.weather.model.setup;

import java.util.List;

public class CreateConditions {

    private int minutesBetweenSamples;
    
    private List<Conditions> conditions;

    public int getMinutesBetweenSamples() {
        return minutesBetweenSamples;
    }

    public void setMinutesBetweenSamples(int minutesBetweenSamples) {
        this.minutesBetweenSamples = minutesBetweenSamples;
    }

    public List<Conditions> getConditions() {
        return conditions;
    }

    public void setConditions(List<Conditions> conditions) {
        this.conditions = conditions;
    }
    
    
}
