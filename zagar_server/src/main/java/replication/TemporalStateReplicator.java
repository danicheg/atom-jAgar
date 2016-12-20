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
import protocol.GameConstraints;
import protocol.model.Blob;
import protocol.model.Cell;
import protocol.model.Food;
import protocol.model.Virus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static utils.generators.ModelToProtocolGenerator.*;

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
                        int totalSize = 0;
                        for (model.Cell cell : cells) {
                            totalSize = totalSize + cell.getMass();
                            x = x + cell.getX();
                            y = y + cell.getY();
                        }
                        double avgX = x / cells.size();
                        double avgY = y / cells.size();
//
//                        double zoomm = -1;
//                        zoomm = GameConstraints.FRAME_HEIGHT / (1024 / Math.pow(Math.min(64.0 / totalSize, 1), 0.4));
//                        double zoom = 0;
//
//                        if (zoomm > 1) {
//                            zoomm = 1;
//                        }
//
//                        if (zoomm == -1) {
//                            zoomm = zoom;
//                        }
//                        zoom += (zoomm - zoom) / 40f;

                        double borderLeft = (avgX - GameConstraints.FRAME_WIDTH / 2) - 300;
                        double borderRight = (avgX + GameConstraints.FRAME_WIDTH / 2) + 300;
                        double borderTop = (avgY + GameConstraints.FRAME_HEIGHT / 2) + 200;
                        double borderBottom = (avgY - GameConstraints.FRAME_HEIGHT / 2) - 200;

                        Field field = gameSession.sessionField();

                        Food[] food = generateProtocolFoodFromModel(field.getFoods(), borderTop, borderBottom, borderLeft, borderRight);
                        Virus[] viruses = generateProtocolVirusesFromModel(field.getViruses(), borderTop, borderBottom, borderLeft, borderRight);
                        Cell[] cellsP = generateProtocolCellsFromModel(gameSession, borderTop, borderBottom, borderLeft, borderRight);
                        Blob[] blobs = generateProtocolBlobsFromModel(field.getBlobs(), borderTop, borderBottom, borderLeft, borderRight);
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
