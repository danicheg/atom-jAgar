package utils;

import protocol.GameConstraints;
import model.Player;
import model.Cell;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomPlayerPlacer implements PlayerPlacer {

    @Override
    public void place(@NotNull Player player) {
        Random random = new Random();
        for (Cell playerCell : player.getCells()) {
            playerCell.setX(playerCell.getRadius() +
                    random.nextInt(Math.round(GameConstraints.FIELD_WIDTH - 2 * playerCell.getRadius())));
            playerCell.setY(playerCell.getRadius() +
                    random.nextInt(Math.round(GameConstraints.FIELD_HEIGHT - 2 * playerCell.getRadius())));
        }
    }

}
