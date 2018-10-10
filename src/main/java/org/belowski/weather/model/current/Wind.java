package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder={"speed", "gusts", "direction"})
public class Wind {

    private Speed speed;
    
    private Direction direction;

    private String gusts = "";
    
    public Wind(Speed speed, Direction direction) {
        super();
        this.speed = speed;
        this.direction = direction;
    }

    public Wind() {
        super();
    }

    @XmlElement(name = "speed")
    public Speed getSpeed() {
        return speed;
    }

    public void setSpeed(Speed speed) {
        this.speed = speed;
    }
    
    @XmlElement(name = "gusts")
    public String getGusts() {
        return gusts;
    }

    public void setGusts(String gusts) {
        this.gusts = gusts;
    }

    @XmlElement(name = "direction")
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String toString() {
        return "Wind [speed=" + speed + ", direction=" + direction + "]";
    }
}
