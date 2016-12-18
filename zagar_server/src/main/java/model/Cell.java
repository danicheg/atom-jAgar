package model;

import utils.IDGenerator;
import utils.SequentialIDGenerator;

public class Cell extends GameUnit {

    private final int id;

    public static final IDGenerator idGenerator = new SequentialIDGenerator();

    public Cell(Location x) {
        super(x, GameConstants.DEFAULT_PLAYER_CELL_MASS);
        this.id = idGenerator.next();
    }

    public Cell() {
        super(new Location(), GameConstants.DEFAULT_PLAYER_CELL_MASS);
        this.id = idGenerator.next();
    }

    public int getId() {
        return id;
    }

    public static protocol.model.Cell[] generateProtocolCellsFromModel(GameSession gameSession) {
        int numberOfCellsInSession = 0;
        for (Player player : gameSession.sessionPlayersList()) {
            numberOfCellsInSession += player.getCells().size();
        }
        protocol.model.Cell[] cells = new protocol.model.Cell[numberOfCellsInSession];
        int i = 0;
        for (Player player : gameSession.sessionPlayersList()) {
            for (Cell playerCell : player.getCells()) {
                cells[i] = new protocol.model.Cell(
                        playerCell.getId(),
                        player.getId(),
                        playerCell.getMass(),
                        playerCell.getX(),
                        playerCell.getY()
                );
                i++;
            }
        }
        return cells;
    }
}
