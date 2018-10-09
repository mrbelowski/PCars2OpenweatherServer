package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Clouds {

    private int value;

    private String name;

    public Clouds() {
        super();
    }

    public Clouds(int value) {
        super();
        this.value = value;
        // set the name from the value?
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
}
