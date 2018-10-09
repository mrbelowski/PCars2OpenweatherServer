package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

public class Sun {

    private String rise;
    
    private String set;

    public Sun() {
        super();
    }

    public Sun(String rise, String set) {
        super();
        this.rise = rise;
        this.set = set;
    }

    @XmlAttribute
    public String getRise() {
        return rise;
    }

    public void setRise(String rise) {
        this.rise = rise;
    }

    @XmlAttribute
    public String getSet() {
        return set;
    }

    public void setSet(String set) {
        this.set = set;
    }
    
    
}
