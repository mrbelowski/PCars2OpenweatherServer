package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class WindDirection {

    private float deg;

    private String code;

    private String name;

    public WindDirection() {
        super();
    }

    public WindDirection(float deg) {
        super();
        this.deg = deg;
        // set the code and name from the deg
    }

    @XmlAttribute
    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
