package model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;
import protocol.model.Functions;

import java.awt.Color;
import java.util.List;

import static java.lang.Math.PI;
import static java.lang.Math.sqrt;

public class Blob extends GameUnit {

    @NotNull
    private static final Logger log = LogManager.getLogger(Blob.class);

    private Vector vector;

    public Blob(@NotNull Location mouseLocation, Cell parent) {
        super(Color.LIGHT_GRAY, calculateDislocation(parent, mouseLocation), GameConstants.BLOB_SPEED, GameConstants.BLOB_MASS_CREATE);
        vector = Vector.createVector(parent.getLocation(), getLocation());
        log.info(toString() + " created");
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
                2  + Functions.calculateRadius(GameConstants.BLOB_MASS_CREATE));
        return vector.getEnd(parent.getLocation());
    }

    public static protocol.model.Blob[] generateProtocolBlobsFromModel(ConcurrentHashSet<Blob> blobsIn) {
        protocol.model.Blob[] blobsOut = new protocol.model.Blob[blobsIn.size()];
        int inc = 0;
        for (model.Blob blobGot : blobsIn) {
            blobsOut[inc] = new protocol.model.Blob(blobGot.getId(),
                    blobGot.getMass(),
                    blobGot.getLocation().getX(),
                    blobGot.getLocation().getY());
            inc++;
        }
        return blobsOut;
    }
}