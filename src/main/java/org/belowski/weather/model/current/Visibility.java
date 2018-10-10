package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

public class Visibility {

    private int value;

    public Visibility(int value) {
        super();
        if (value == 0) {
            this.value = 2000;
        }
        else {
          this.value = value;
        }
    }

    public Visibility() {
        super();
    }

    @XmlAttribute
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Visibility [value=" + value + "]";
    }
}