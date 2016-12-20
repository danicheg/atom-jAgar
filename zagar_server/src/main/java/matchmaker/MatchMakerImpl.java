package matchmaker;

import model.Field;
import model.GameSession;
import model.GameSessionImpl;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.GameConstraints;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchMakerImpl implements MatchMaker {

    @NotNull
    private static final Logger LOG = LogManager.getLogger(MatchMakerImpl.class);
    @NotNull
    private final List<GameSession> activeGameSessions = new CopyOnWriteArrayList<>();

    @Override
    public void joinGame(@NotNull Player player) {
        final Optional<GameSession> sessionOptional = activeGameSessions.stream()
                .filter(session -> session.sessionPlayersList().size() < GameConstraints.MAX_PLAYERS_IN_SESSION)
                .findFirst();
        if (sessionOptional.isPresent()) {
            final GameSession freeSession = sessionOptional.get();
            freeSession.join(player);
            LOG.info(player + " joined to session" + freeSession);
        } else {
            GameSession newGameSession = new GameSessionImpl(new Field());
            activeGameSessions.add(newGameSession);
            newGameSession.join(player);
            LOG.info(player + " joined to new game session" + newGameSession);
        }
    }

    @NotNull
    @Override
    public List<GameSession> getActiveGameSessions() {
        return new CopyOnWriteArrayList<>(activeGameSessions);
    }

}
