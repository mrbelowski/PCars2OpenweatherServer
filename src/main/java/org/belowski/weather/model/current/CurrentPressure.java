package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"unit", "value"})
public class CurrentPressure {

    private String unit;

    private float value;

    public CurrentPressure() {
        super();
    }

    public CurrentPressure(String unit, float value) {
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
