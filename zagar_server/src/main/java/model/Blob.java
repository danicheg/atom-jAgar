package model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;
import protocol.utils.Calculator;
import protocol.GameConstraints;

import java.awt.Color;

public class Blob extends GameUnit {

    @NotNull
    private static final Logger LOG = LogManager.getLogger(Blob.class);

    private Vector vector;

    public Blob(@NotNull Location mouseLocation, Cell parent) {
        super(Color.LIGHT_GRAY, calculateDislocation(parent, mouseLocation),
                GameConstraints.BLOB_SPEED, GameConstraints.BLOB_MASS_CREATE);
        vector = Vector.createVector(parent.getLocation(), getLocation());
        LOG.info(toString() + " created");
    }

    public void makeMove() {
        vector = vector.extend(getSpeed());
        setLocation(vector.getEnd(getLocation()));
        decreaseSpeed(0.66f);
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

    private static Location calculateDislocation(Cell parent, Location mouseLocation) {
        Vector vector = Vector.createVector(parent.getLocation(), mouseLocation).normalize().extend(parent.getRadius() +
                2  + Calculator.calculateRadius(GameConstraints.BLOB_MASS_CREATE));
        return vector.getEnd(parent.getLocation());
    }
}