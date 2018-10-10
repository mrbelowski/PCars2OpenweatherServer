package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"lastUpdate", "calcTime", "nextUpdate"})
public class Meta {

    String lastUpdate = "";
    float calcTime = 0.01f;
    String nextUpdate = "";
    
    @XmlElement(name = "lastupdate")
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    @XmlElement(name = "calctime")
    public float getCalcTime() {
        return calcTime;
    }
    public void setCalcTime(float calcTime) {
        this.calcTime = calcTime;
    }
    
    @XmlElement(name = "nextupdate")
    public String getNextUpdate() {
        return nextUpdate;
    }
    public void setNextUpdate(String nextUpdate) {
        this.nextUpdate = nextUpdate;
    }
}
