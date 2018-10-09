package org.belowski.weather.model;

import javax.xml.bind.annotation.XmlAttribute;

public class Humidity {

    private String unit;

    private int value;

    public Humidity() {
        super();
    }

    public Humidity(int value, String unit) {
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
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Humidity [unit=" + unit + ", value=" + value + "]";
    }

}
