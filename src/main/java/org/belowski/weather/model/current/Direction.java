package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Direction {

    private int value;
    
    private String code;
    
    private String name;

    public Direction(int value) {
        super();
        this.value = value;
        // set name and code
    }

    public Direction() {
        super();
    }

    @XmlAttribute
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @XmlAttribute
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
