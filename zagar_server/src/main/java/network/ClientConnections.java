package network;

import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientConnections {

    private static final Logger LOG = LogManager.getLogger(ClientConnections.class);

    private final ConcurrentHashMap<Player, Session> connections = new ConcurrentHashMap<>();

    public Session registerConnection(Player player, Session session) {
        LOG.info("Connection registered [" + player + "]");
        return connections.putIfAbsent(player, session);
    }

    public boolean removeConnection(Player player) {
        LOG.info("Connection removed [" + player + "]");
        return connections.remove(player) != null;
    }

    public Set<Map.Entry<Player, Session>> getConnections() {
        return connections.entrySet();
    }

}
