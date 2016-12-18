package model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.util.List;

public class Blob extends GameUnit {

    @NotNull
    private static final Logger log = LogManager.getLogger(Blob.class);

    private Vector vector;

    public Blob(@NotNull Color color, @NotNull Location initiallyLocation, @NotNull Location nextLcoation, float speed, int mass) {
        super(color, initiallyLocation, speed, mass);
        vector = Vector.createVector(initiallyLocation, nextLcoation).normalize();
        log.info(toString() + " created");
    }

    public void makeMove() {
        setLocation(vector.extend(getSpeed()).getEnd(getLocation()));
        setSpeed(getSpeed() - 2);
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