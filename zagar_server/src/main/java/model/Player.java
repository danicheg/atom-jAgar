package model;

import entities.user.UserEntity;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.IDGenerator;
import utils.SequentialIDGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {

    public static final IDGenerator idGenerator = new SequentialIDGenerator();

    private final int id;
    @NotNull
    private final List<Cell> cells;
    @NotNull
    private String name;
    @Nullable
    private GameSession session;

    private UserEntity user;

    public Player(int playerId, @NotNull String playerName) {
        id = playerId;
        name = playerName;
        cells = new CopyOnWriteArrayList<>();
        addCell(new Cell(new Location(0, 0)));
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

    public void addCell(@NotNull Cell cell) {
        cells.add(cell);
    }

    public void removeCell(@NotNull Cell cell) {
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
    public List<Cell> getCells() {
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

    public int getMass() {
        int massResult = 0;
        for (Cell c : cells) {
            massResult += c.getMass();
        }
        return massResult;
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

    public Cell getMostMassiveCell() {
        Cell cellResult = null;
        for (Cell cell : cells) {
            if (cellResult == null) {
                cellResult = cell;
            } else if (cellResult.getMass() < cell.getMass()) {
                cellResult = cell;
            }
        }
        return cellResult;
    }

    public static Player getPlayerByName(String name) {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            for (Player player : gameSession.getPlayers()) {
                if (player.getName().equals(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    public static boolean removeUserFromSession(Player playerGot) {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            for (Player player : gameSession.getPlayers()) {
                if (player.equals(playerGot)) {
                    gameSession.leave(playerGot);
                    return true;
                }
            }
        }
        return false;
    }

}
