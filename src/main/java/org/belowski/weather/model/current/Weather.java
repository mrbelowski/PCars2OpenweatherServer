package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.belowski.weather.model.WeatherNumber;
import org.belowski.weather.model.WeatherNumber.ConditionType;

@XmlType(propOrder={"number", "value", "icon"})
public class Weather {

    private int number;
    
    private ConditionType conditionType;
    
    private String value;
    
    private String icon;
    
    public static Weather generate(float rainAmount, int visibility, int clouds) {
        return new Weather(WeatherNumber.getConditionType(rainAmount, visibility, clouds), "auto generated", "01d"); 
    }
    
    public Weather(ConditionType conditionType) {
        super();
        this.conditionType = conditionType;
        this.number = WeatherNumber.CONDITION_IDS.get(conditionType);
    }
    
    private Weather(ConditionType conditionType, String value, String icon) {
        super();
        this.conditionType = conditionType;
        this.number = WeatherNumber.CONDITION_IDS.get(conditionType);
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

    public ConditionType getConditionType() {
        return conditionType;
    }
}
