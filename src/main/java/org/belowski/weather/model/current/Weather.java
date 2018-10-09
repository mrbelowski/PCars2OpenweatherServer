package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Weather {

    private int number;
    
    private String value;
    
    private String icon;
    
    public Weather(int number, String value, String icon) {
        super();
        this.number = number;
        this.value = value;
        this.icon = icon;
    }

    public Weather() {
        super();
    }

    @XmlAttribute(name = "number")
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @XmlAttribute(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @XmlAttribute(name = "icon")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
