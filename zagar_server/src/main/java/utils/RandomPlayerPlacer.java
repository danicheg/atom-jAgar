package utils;

import model.Field;
import model.GameConstants;
import model.Player;
import model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author apomosov
 */
public class RandomPlayerPlacer implements PlayerPlacer {

    @NotNull
    private final Field field;

    public RandomPlayerPlacer(@NotNull Field fieldToPlace) {
        field = fieldToPlace;
    }

    @Override
    public void place(@NotNull Player player) {
        assert player.getCells().size() == 1;
        Random random = new Random();
        for (Cell playerCell : player.getCells()) {
            playerCell.setX(playerCell.getRadius() +
                    random.nextInt(Math.round(GameConstants.FIELD_WIDTH - 2 * playerCell.getRadius())));
            playerCell.setY(playerCell.getRadius() +
                    random.nextInt(Math.round(GameConstants.FIELD_HEIGHT - 2 * playerCell.getRadius())));
        }
    }

}
