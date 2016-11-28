package model;

import org.jetbrains.annotations.NotNull;
import utils.IDGenerator;
import utils.SequentialIDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author apomosov
 */
public class Player {
    public static final IDGenerator idGenerator = new SequentialIDGenerator();
    private final int id;
    @NotNull
    private final List<PlayerCell> cells = new ArrayList<>();
    @NotNull
    private String name;

    public Player(int id, @NotNull String name) {
        this.id = id;
        this.name = name;
        addCell(new PlayerCell(Cell.idGenerator.next(), 0, 0));
    }

    public void addCell(@NotNull PlayerCell cell) {
        cells.add(cell);
    }

    public void removeCell(@NotNull PlayerCell cell) {
        cells.remove(cell);
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public List<PlayerCell> getCells() {
        return cells;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @NotNull
    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
