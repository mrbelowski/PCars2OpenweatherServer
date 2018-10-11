package org.belowski.weather.model.forecast;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class Forecast {

    private List<Time> times;
    
    public Forecast(List<Time> times) {
        super();
        this.times = times;
    }

    public Forecast() {
        super();
    }

    @XmlElement(name = "time")
    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Forecast [times=\n");
        for (Time time : times) {
            sb.append(time.toString()).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }
}
