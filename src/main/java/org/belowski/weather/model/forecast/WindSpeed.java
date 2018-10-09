package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

public class WindSpeed {

    private float mps;

    private String name;

    public WindSpeed() {
        super();
    }

    public WindSpeed(float mps) {
        super();
        this.mps = mps;
        // set the name from the mps
    }

    @XmlAttribute
    public float getMps() {
        return mps;
    }

    public void setMps(float mps) {
        this.mps = mps;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
