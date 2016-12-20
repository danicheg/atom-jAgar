package replication;

import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.Field;
import model.GameSession;
import model.Location;
import model.Player;
import model.Vector;
import network.ClientConnections;
import network.packets.PacketReplicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import protocol.GameConstraints;
import protocol.model.Blob;
import protocol.model.Cell;
import protocol.model.Food;
import protocol.model.Virus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemporalStateReplicator implements Replicator {

    private static final Logger LOG = LogManager.getLogger(TemporalStateReplicator.class);

    @Override
    public void replicateState() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            List<Player> players = gameSession.sessionPlayersList();

            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.sessionPlayersList().contains(connection.getKey()) && connection.getValue().isOpen()) {
                    for (Player player : gameSession.sessionPlayersList()) {
                List<model.Cell> cells = player.getCells();
                double x = 0;
                double y = 0;
                for (model.Cell cell : cells) {
                    x = x + cell.getX();
                    y = y + cell.getY();
                }
                double avgX = x / cells.size();
                double avgY = y / cells.size();

                double borderLeft = avgX - GameConstraints.FRAME_WIDTH / 2 - 300;
                double borderRight = avgX + GameConstraints.FRAME_WIDTH / 2 + 300;
                double borderTop = avgY  + GameConstraints.FRAME_HEIGHT / 2 + 200;
                double borderBottom = avgY - GameConstraints.FRAME_HEIGHT / 2 - 200;

            Field field = gameSession.sessionField();

            Food[] food = model.Food.generateProtocolFoodFromModel(field.getFoods(), borderTop, borderBottom, borderLeft, borderRight);
            Virus[] viruses = model.Virus.generateProtocolVirusesFromModel(field.getViruses(), borderTop, borderBottom, borderLeft, borderRight);
            Cell[] cellsP = model.Cell.generateProtocolCellsFromModel(gameSession, borderTop, borderBottom, borderLeft, borderRight);
            Blob[] blobs = model.Blob.generateProtocolBlobsFromModel(field.getBlobs(), borderTop, borderBottom, borderLeft, borderRight);
                    try {
                        new PacketReplicate(cellsP, food, viruses, blobs).write(connection.getValue());
                    } catch (IOException e) {
                        LOG.error("Exception in creating PacketReplicate: " + e);
                    }
                }
            }

            }
        }
    }
}
