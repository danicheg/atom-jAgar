package matchmaker;

import server.entities.user.User;
import server.session.GameSession;
import model.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Provides (searches or creates) {@link GameSession} for {@link Player}
 *
 * @author Alpi
 */
public interface MatchMaker {
  /**
   * Searches available game session or creates new one
   * @param user player to join the game session
   */
  void joinGame(@NotNull User user);

  /**
   * @return Currently open game sessions
   */
  @NotNull
  List<GameSession> getActiveGameSessions();
}
