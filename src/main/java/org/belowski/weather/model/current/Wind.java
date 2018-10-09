package org.belowski.weather.model.current;

import javax.xml.bind.annotation.XmlElement;

public class Wind {

    private Speed speed;
    
    private Direction direction;

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

    @XmlElement(name = "direction")
    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    
    
}
