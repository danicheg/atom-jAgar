package utils;

import model.Field;
import model.Player;
import model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author apomosov
 */
public class SimplePlayerPlacer implements PlayerPlacer {

    @NotNull
    private final Field field;

    public SimplePlayerPlacer(@NotNull Field fieldToPlace) {
        field = fieldToPlace;
    }

    @Override
    public void place(@NotNull Player player) {
        assert player.getCells().size() == 1;
        Random random = new Random();
        for (Cell playerCell : player.getCells()) {
            playerCell.setX(5);
            playerCell.setY(5);
        }
    }

}
