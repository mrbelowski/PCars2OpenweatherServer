package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"value", "name"})
public class CurrentClouds {

    private int value;

    private String name;

    public CurrentClouds() {
        super();
    }

    public CurrentClouds(int value) {
        super();
        this.value = value;
        // set the name from the value
        this.name = "broken clouds";
    }

    @XmlAttribute
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
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
        return "Clouds [value=" + value + ", name=" + name + "]";
    }
    
}
