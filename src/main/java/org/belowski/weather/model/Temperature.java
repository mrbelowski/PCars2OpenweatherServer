package org.belowski.weather.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Temperature {

    private String unit;

    private float value;

    private float min;

    private float max;

    public Temperature() {
        super();
    }

    public Temperature(String unit, float value, float min, float max) {
        super();
        this.unit = unit;
        this.value = value;
        this.min = min;
        this.max = max;
    }

    @XmlAttribute
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @XmlAttribute
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @XmlAttribute
    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    @XmlAttribute
    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }
}
