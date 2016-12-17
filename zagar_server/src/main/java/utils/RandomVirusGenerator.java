package utils;

import model.Field;
import model.GameConstants;
import model.Location;
import model.Virus;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomVirusGenerator implements VirusGenerator {

    @NotNull private final Field field;
    private final int numberOfViruses;

    public RandomVirusGenerator(@NotNull Field fieldToPlace, int numberOfViruses) {
        field = fieldToPlace;
        this.numberOfViruses = numberOfViruses;
    }

    @Override
    public void generate() {
        Random random = new Random();
        int virusRadius = (int) Math.sqrt(GameConstants.VIRUS_MASS / Math.PI);
        for (int i = 0; i < numberOfViruses; i++) {
            new Virus(new Location(virusRadius + random.nextInt(GameConstants.FIELD_WIDTH - 2 * virusRadius),
                    virusRadius + random.nextInt(GameConstants.FIELD_HEIGHT - 2 * virusRadius)));
        }
    }

}
