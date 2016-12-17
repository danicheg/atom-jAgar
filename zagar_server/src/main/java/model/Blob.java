package model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;

public class Blob extends GameUnit {

    @NotNull
    private static final Logger log = LogManager.getLogger(Blob.class);

    public Blob(@NotNull Color color, @NotNull Location location, float speed, int mass) {
        super(color, location, speed, mass);
        log.info(toString() + " created");
    }

    @Override
    public String toString() {
        return "Blob{" +
                "location=" + this.getLocation() +
                ", color=" + this.getColor() +
                ", speed=" + this.getSpeed() +
                ", mass=" + this.getMass() +
                ", radius=" + this.getRadius() +
                '}';
    }
}