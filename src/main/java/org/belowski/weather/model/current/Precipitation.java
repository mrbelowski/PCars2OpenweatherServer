package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Precipitation {
    
    private String unit = "3h";

    private String mode;

    // value is some millimetres-per-3hours I think
    private float value;

    public Precipitation() {
        super();
    }

    public Precipitation(float value) {
        super();
        this.value = value;
        // mode will be "no", "rain", or "snow" I think
        // so just infer this here
        this.mode = value == 0 ? "no" : "rain";
    }

    @XmlAttribute
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @XmlAttribute
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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
        return "Precipitation [unit=" + unit + ", mode=" + mode + ", value=" + value + "]";
    }
    
    
}
