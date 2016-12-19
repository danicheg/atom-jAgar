package model;

import org.jetbrains.annotations.NotNull;
import protocol.enums.GameConstraints;
import utils.FoodGenerator;
import utils.IDGenerator;
import utils.PlayerPlacer;
import utils.RandomPlayerPlacer;
import utils.RandomVirusGenerator;
import utils.SequentialIDGenerator;
import utils.UniformFoodGenerator;
import utils.VirusGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameSessionImpl implements GameSession {

    private static final IDGenerator idGenerator = new SequentialIDGenerator();

    private final int id = idGenerator.next();
    @NotNull
    private final Field field;
    @NotNull
    private final List<Player> players;
    @NotNull
    private final PlayerPlacer playerPlacer;

    public GameSessionImpl(@NotNull Field fieldToPlace) {
        FoodGenerator foodGenerator = new UniformFoodGenerator(
                fieldToPlace,
                GameConstraints.FOOD_PER_SECOND_GENERATION,
                GameConstraints.MAX_FOOD_ON_FIELD
        );
        VirusGenerator virusGenerator = new RandomVirusGenerator(fieldToPlace, GameConstraints.NUMBER_OF_VIRUSES);
        playerPlacer = new RandomPlayerPlacer(fieldToPlace);
        field = fieldToPlace;
        players = new ArrayList<>();
        field.generatePrimaryState();
        foodGenerator.startGenerating();
        virusGenerator.generate();
    }

    @Override
    public void join(@NotNull Player player) {
        players.add(player);
        player.setSession(this);
        this.playerPlacer.place(player);
    }

    @Override
    public void leave(@NotNull Player player) {
        players.remove(player);
        player.setSession(null);
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

}
