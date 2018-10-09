package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class LastUpdate {

    private String value;

    @XmlAttribute(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
