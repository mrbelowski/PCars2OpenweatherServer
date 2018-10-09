package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlAttribute;

// do we need this?
public class Symbol {

    private int number;

    private String name;

    private String var;

    public Symbol() {
        super();
    }

    public Symbol(int number, String name, String var) {
        super();
        this.number = number;
        this.name = name;
        this.var = var;
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
}
