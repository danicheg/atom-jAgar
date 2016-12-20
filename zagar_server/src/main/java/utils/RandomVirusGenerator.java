package utils;

import model.Field;
import model.Location;
import model.Virus;
import org.jetbrains.annotations.NotNull;
import protocol.GameConstraints;
import protocol.utils.Calculator;

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
        int virusRadius = (int) Calculator.calculateRadius(GameConstraints.INITIAL_VIRUS_MASS);
        for (int i = 0; i < numberOfViruses; i++) {
            field.addVirus(new Virus(new Location(
                    virusRadius + random.nextInt(GameConstraints.FIELD_WIDTH - 2 * virusRadius),
                    virusRadius + random.nextInt(GameConstraints.FIELD_HEIGHT - 2 * virusRadius)
            )));
        }
    }

}
