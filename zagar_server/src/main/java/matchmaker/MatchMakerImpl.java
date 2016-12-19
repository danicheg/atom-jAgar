package matchmaker;

import model.Field;
import model.GameSession;
import model.GameSessionImpl;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Creates {@link GameSession} for single player
 *
 * @author Alpi
 */
public class MatchMakerImpl implements MatchMaker {
    @NotNull
    private final Logger log = LogManager.getLogger(MatchMakerImpl.class);
    @NotNull
    private final List<GameSession> activeGameSessions = new CopyOnWriteArrayList<>();

    /**
     * Creates new GameSession for single player
     *
     * @param player single player
     */
    @Override
    public void joinGame(@NotNull Player player) {
        GameSession newGameSession = createNewGame();
        activeGameSessions.add(newGameSession);
        newGameSession.join(player);
        if (log.isInfoEnabled()) {
            log.info(player + " joined " + newGameSession);
        }
    }

    @NotNull
    public CopyOnWriteArrayList<GameSession> getActiveGameSessions() {
        return new CopyOnWriteArrayList<>(activeGameSessions);
    }

    /**
     * @return new GameSession
     */
    private GameSession createNewGame() {
        return new GameSessionImpl(new Field());
    }
}
