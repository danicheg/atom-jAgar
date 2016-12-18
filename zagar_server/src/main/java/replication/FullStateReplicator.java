package replication;

import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.Field;
import model.GameSession;
import model.Player;
import network.ClientConnections;
import network.packets.PacketReplicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import protocol.model.Blob;
import protocol.model.Food;
import protocol.model.Virus;
import protocol.model.Cell;

import java.io.IOException;
import java.util.Map;

public class FullStateReplicator implements Replicator {

    private static final Logger LOG = LogManager.getLogger(FullStateReplicator.class);

    @Override
    public void replicateState() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            Field field = gameSession.sessionField();

            Food[] food = model.Food.generateProtocolFoodFromModel(field.getFoods());
            Virus[] viruses = model.Virus.generateProtocolVirusesFromModel(field.getViruses());
            Cell[] cells = model.Cell.generateProtocolCellsFromModel(gameSession);
            Blob[] blobs = model.Blob.generateProtocolBlobsFromModel(field.getBlobs());

            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.sessionPlayersList().contains(connection.getKey()) && connection.getValue().isOpen()) {
                    try {
                        new PacketReplicate(cells, food, viruses, blobs).write(connection.getValue());
                    } catch (IOException e) {
                        LOG.error("Exception in creating PacketReplicate: " + e);
                    }
                }
            }
        }

    }
}
