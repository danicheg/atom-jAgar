package replication;


import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.GameSession;
import model.Player;
import network.ClientConnections;
import network.packets.PacketLeaderBoard;;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LeaderBoardSender implements LeaderBoarder {

    private static final Logger LOG = LogManager.getLogger(LeaderBoardSender.class);

    @Override
    public void replicateLeaderBoard() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            String[] leaders = new String[10];
            List<Player> players = gameSession.getPlayers();
            if (players.size() < 10) {
                for (int j = 0; j < players.size(); j++) {
                    leaders[j] = players.get(j).getName();
                }
            } else {
                for (int j = 0; j < 10; j++) {
                    leaders[j] = players.get(j).getName();
                }
            }

            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.getPlayers().contains(connection.getKey()) && connection.getValue().isOpen()) {
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
