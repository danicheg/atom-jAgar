package model;

import org.eclipse.jetty.util.ConcurrentHashSet;
import protocol.GameConstraints;
import utils.generators.IDGenerator;
import utils.generators.SequentialIDGenerator;

public class Cell extends GameUnit {

    private final int id;

    private final Player owner;

    public static final IDGenerator idGenerator = new SequentialIDGenerator();

    public Cell(Location x, Player owner) {
        super(x, GameConstraints.DEFAULT_PLAYER_CELL_MASS);
        this.owner = owner;
        this.id = idGenerator.next();
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return owner.getName();
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
                        player.getName(),
                        playerCell.getMass(),
                        playerCell.getX(),
                        playerCell.getY(),
                        player.getColor().getRed(),
                        player.getColor().getGreen(),
                        player.getColor().getBlue()
                );
                i++;
            }
        }
        return cells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cell player = (Cell) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
