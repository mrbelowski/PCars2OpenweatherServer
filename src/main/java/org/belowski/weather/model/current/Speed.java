package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Speed {

    private float value;
    
    private String name;

    public Speed() {
        super();
    }

    public Speed(float value) {
        super();
        this.value = value;
        // set the name from the speed
        this.name = "gentle breeze";
    }

    @XmlAttribute
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Speed [value=" + value + ", name=" + name + "]";
    }
    
}
