package org.belowski.weather.model.forecast;

import javax.xml.bind.annotation.XmlAttribute;

import org.belowski.weather.model.ConditionsConstants;
import org.belowski.weather.model.ConditionsConstants.ConditionType;

public class Symbol {
    
    private ConditionType conditionType;

    private int number;

    private String name;

    private String var;
    
    public static Symbol generate(float rainAmount, int visibility, int clouds, boolean isDay) {
        ConditionType type = ConditionsConstants.getConditionType(rainAmount, visibility, clouds);
        return new Symbol(type, isDay); 
    }

    public Symbol() {
        super();
    }

    public Symbol(ConditionType conditionType, boolean isDay) {
        super();
        this.conditionType = conditionType;
        this.number = conditionType.getId();
        this.name = conditionType.getDescription();
        this.var = conditionType.getSymbol(isDay);
    }
    
    @XmlAttribute
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    @Override
    public String toString() {
        return "Symbol [conditionType=" + conditionType + ", number=" + number + "]";
    }
}
