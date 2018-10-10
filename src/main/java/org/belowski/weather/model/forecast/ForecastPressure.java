package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"value", "unit"})
public class ForecastPressure {

    private String unit;

    private float value;

    public ForecastPressure() {
        super();
    }

    public ForecastPressure(String unit, float value) {
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
