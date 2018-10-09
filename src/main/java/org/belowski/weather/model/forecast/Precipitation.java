package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

public class Precipitation {

    private String unit = "3h";

    private float value;

    private String type;

    public Precipitation() {
        super();
    }

    public Precipitation(float value) {
        super();
        this.value = value;
        // type will be "no", "rain", or "snow" I think
        // so just infer this here
        this.type = value == 0 ? "no" : "rain";
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
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
