package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

public class ForecastClouds {

    private String value;

    private int all;

    private String unit;

    public ForecastClouds() {
        super();
    }

    public ForecastClouds(int all, String unit) {
        super();
        this.all = all;
        this.unit = unit;
        // set value from all
        this.value = "few clouds";
    }

    @XmlAttribute
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlAttribute
    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }

    @XmlAttribute
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Clouds [value=" + value + ", all=" + all + ", unit=" + unit + "]";
    }
    
}
