package org.belowski.weather.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Pressure {

    private String unit;

    private float value;

    public Pressure() {
        super();
    }

    public Pressure(String unit, float value) {
        super();
        this.unit = unit;
        this.value = value;
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

    @Override
    public String toString() {
        return "Pressure [unit=" + unit + ", value=" + value + "]";
    }

}
