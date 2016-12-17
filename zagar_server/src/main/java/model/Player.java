package model;

import entities.user.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.IDGenerator;
import utils.SequentialIDGenerator;

import java.util.ArrayList;
import java.util.List;

public class Player {

    public static final IDGenerator idGenerator = new SequentialIDGenerator();

    private final int id;
    @NotNull
    private final List<PlayerCell> cells;
    @NotNull
    private String name;
    @Nullable
    private GameSession session;

    private UserEntity user;

    public Player(int playerId, @NotNull String playerName) {
        id = playerId;
        name = playerName;
        cells = new ArrayList<>();
        addCell(new PlayerCell(Cell.idGenerator.next(), 0, 0));
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setSession(@Nullable GameSession session) {
        this.session = session;
    }

    @Nullable
    public GameSession getSession() {
        return this.session;
    }

    private void addCell(@NotNull PlayerCell cell) {
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

    public int getScore() {
        int newScore = 0;
        for (Cell c : cells) {
            newScore += (c.getMass() * c.getMass()) / 100;
        }
        return newScore;
}

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
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
