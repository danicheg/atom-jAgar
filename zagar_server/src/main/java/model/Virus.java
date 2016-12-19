package model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static protocol.enums.GameConstraints.INITIAL_VIRUS_MASS;

public class Virus extends GameUnit {

    private static final Logger LOG = LogManager.getLogger(Virus.class);

    public Virus(@NotNull Location location) {
        super(location, INITIAL_VIRUS_MASS);
        if (LOG.isInfoEnabled()) {
            LOG.info(toString() + " created");
        }
    }

    public static protocol.model.Virus[] generateProtocolVirusesFromModel(List<Virus> virusesIn) {
        protocol.model.Virus[] virusesOut = new protocol.model.Virus[virusesIn.size()];
        int k = 0;
        for (model.Virus virusGot : virusesIn) {
            virusesOut[k] = new protocol.model.Virus(virusGot.getId(),
                    virusGot.getMass(),
                    virusGot.getLocation().getX(),
                    virusGot.getLocation().getY());
            k++;
        }
        return virusesOut;
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
