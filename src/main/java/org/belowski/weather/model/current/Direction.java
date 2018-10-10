package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"value", "code", "name"})
public class Direction {

    private float value;
    
    private String code;
    
    private String name;

    public Direction(float value) {
        super();
        this.value = value;
        // set name and code
        this.name = "East";
        this.code = "E";
    }

    public Direction() {
        super();
    }

    @XmlAttribute
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
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

    @Override
    public String toString() {
        return "Direction [value=" + value + ", code=" + code + ", name=" + name + "]";
    }
    
}
