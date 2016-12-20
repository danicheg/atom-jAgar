package model;

import dao.LeaderboardDao;
import entities.leaderboard.Leaderboard;
import org.jetbrains.annotations.NotNull;
import protocol.GameConstraints;
import utils.generators.FoodGenerator;
import utils.generators.IDGenerator;
import utils.PlayerPlacer;
import utils.RandomPlayerPlacer;
import utils.generators.RandomVirusGenerator;
import utils.generators.SequentialIDGenerator;
import utils.generators.UniformFoodGenerator;
import utils.generators.VirusGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameSessionImpl implements GameSession, Comparable<List<Player>> {

    private static final IDGenerator idGenerator = new SequentialIDGenerator();

    private final int id = idGenerator.next();
    @NotNull
    private final Field field;
    @NotNull
    private final List<Player> players;
    @NotNull
    private final PlayerPlacer playerPlacer;

    @NotNull
    private Leaderboard leaderboard;

    public GameSessionImpl(@NotNull Field fieldToPlace) {
        FoodGenerator foodGenerator = new UniformFoodGenerator(
                fieldToPlace,
                GameConstraints.FOOD_PER_SECOND_GENERATION,
                GameConstraints.MAX_FOOD_ON_FIELD
        );
        VirusGenerator virusGenerator = new RandomVirusGenerator(fieldToPlace, GameConstraints.NUMBER_OF_VIRUSES);
        playerPlacer = new RandomPlayerPlacer();
        field = fieldToPlace;
        players = new ArrayList<>();
        field.generateFieldWithFood();
        leaderboard = new Leaderboard();
        new LeaderboardDao().insert(leaderboard);
        foodGenerator.startGenerating();
        virusGenerator.generate();
    }

    @Override
    public void join(@NotNull Player player) {
        players.add(player);
        player.setSession(this);
        leaderboard.addUser(player.getUser());
        new LeaderboardDao().update(leaderboard);
        this.playerPlacer.place(player);
    }

    @Override
    public void leave(@NotNull Player player) {
        leaderboard.deleteUser(player.getUser());
        players.remove(player);
        new LeaderboardDao().update(leaderboard);
        player.setSession(null);
    }

    public Leaderboard getLeaderboard() {
        return this.leaderboard;
    }

    @Override
    public List<Player> sessionPlayersList() {
        return players;
    }

    @NotNull
    @Override
    public Field sessionField() {
        return field;
    }

    @Override
    public String toString() {
        return "GameSessionImpl{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameSessionImpl that = (GameSessionImpl) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(@NotNull List<Player> o) {
        return this.sessionPlayersList().size() - o.size();
    }

}
