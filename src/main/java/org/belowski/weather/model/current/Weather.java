package org.belowski.weather.model.current;

import java.time.LocalDateTime;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.belowski.weather.model.ConditionsConstants;
import org.belowski.weather.model.ConditionsConstants.ConditionType;

@XmlType(propOrder={"number", "value", "icon"})
public class Weather {

    private int number;
    
    private ConditionType conditionType;
    
    private String value;
    
    private String icon;
    
    public static Weather generate(float rainAmount, int visibility, int clouds, LocalDateTime time) {
        ConditionType type = ConditionsConstants.getConditionType(rainAmount, visibility, clouds);
        return new Weather(type, time.getHour() >= 6 && time.getHour() <= 18); 
    }
    
    public Weather(ConditionType conditionType, boolean isDay) {
        super();
        this.conditionType = conditionType;
        this.number = conditionType.getId();
        this.value = conditionType.getDescription();
        this.icon = conditionType.getSymbol(isDay);
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

    public ConditionType getConditionType() {
        return conditionType;
    }
}
