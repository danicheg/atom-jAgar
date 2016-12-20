package replication;

import dao.LeaderboardDao;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.GameSession;
import model.Player;
import network.ClientConnections;
import network.packets.PacketLeaderBoard;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaderBoardSender implements LeaderBoarder {

    private static final Logger LOG = LogManager.getLogger(LeaderBoardSender.class);

    @Override
    public void replicateLeaderBoard() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            String[] leaders = new String[10];
            List<Player> players = gameSession.sessionPlayersList();
            if (players.size() < 10) {
                for (int j = 0; j < players.size(); j++) {
                    leaders[j] = players.get(j).getName();
                }
            } else {
                for (int j = 0; j < 10; j++) {
                    leaders[j] = players.get(j).getName();
                }
            }
            players.sort((o1, o2) -> o2.getScore() - o1.getScore() );
            List<String> leaders2 = new LeaderboardDao().getNLeaders(gameSession.getLeaderboard(), 10).stream()
                    .map(user -> user.getName()).collect(Collectors.toList());
            String[] leaders3 = new String[leaders2.size()];
            for (int i = 0; i < leaders3.length; i++) {
                leaders3[i] = leaders2.get(i);
            }

            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.sessionPlayersList().contains(connection.getKey()) && connection.getValue().isOpen()) {
                    try {
                        new PacketLeaderBoard(leaders).write(connection.getValue());
                    } catch (IOException e) {
                        LOG.error("Exception in creating PacketReplicate: " + e);
                    }
                }
            }
        }
    }
}
