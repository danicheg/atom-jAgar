package model;

import utils.IDGenerator;
import utils.SequentialIDGenerator;

/**
 * @author apomosov
 */
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
}
