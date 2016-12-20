package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static protocol.GameConstraints.INITIAL_VIRUS_MASS;

public class Virus extends GameUnit {

    private static final Logger LOG = LogManager.getLogger(Virus.class);

    public Virus(@NotNull Location location) {
        super(location, INITIAL_VIRUS_MASS);
        if (LOG.isInfoEnabled()) {
            LOG.info(toString() + " created");
        }
    }

    @Override
    public String toString() {
        return "Virus{" +
                "color=" + this.getColor() +
                ", location=" + this.getLocation() +
                ", mass=" + this.getMass() +
                ", speed=" + this.getSpeed() +
                ", radius=" + this.getRadius() +
                '}';
    }

}
