package replication;

import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.Field;
import model.GameSession;
import model.Player;
import model.Cell;
import network.ClientConnections;
import network.packets.PacketReplicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import protocol.model.Blob;
import protocol.model.Food;
import protocol.model.Virus;

import java.io.IOException;
import java.util.Map;

public class FullStateReplicator implements Replicator {

    private static final Logger LOG = LogManager.getLogger(FullStateReplicator.class);

    @Override
    public void replicateState() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            Field field = gameSession.getField();
            Food[] food = new Food[field.getFoods().size()];
            int counter = 0;
            for (model.Food foodGot : field.getFoods()) {
                food[counter] = new Food(foodGot.getLocation().getX(),
                        foodGot.getLocation().getY(),
                        foodGot.getId(),
                        foodGot.getColor().getRed(),
                        foodGot.getColor().getGreen(),
                        foodGot.getColor().getBlue());
                counter++;
            }
            Virus[] viruses = new Virus[field.getViruses().size()];
            int k = 0;
            for (model.Virus virusGot : field.getViruses()) {
                viruses[k] = new Virus(virusGot.getId(),
                        virusGot.getMass(),
                        virusGot.getLocation().getX(),
                        virusGot.getLocation().getY());
                k++;
            }
            int numberOfCellsInSession = 0;
            for (Player player : gameSession.getPlayers()) {
                numberOfCellsInSession += player.getCells().size();
            }
            protocol.model.Cell[] cells = new protocol.model.Cell[numberOfCellsInSession];
            int i = 0;
            for (Player player : gameSession.getPlayers()) {
                for (Cell playerCell : player.getCells()) {
                    cells[i] = new protocol.model.Cell(
                            playerCell.getId(),
                            player.getId(),
                            playerCell.getMass(),
                            playerCell.getX(),
                            playerCell.getY()
                    );
                    i++;
                }
            }

            Blob[] blobs = new Blob[field.getBlobs().size()];
            int inc = 0;
            for (model.Blob blobGot : field.getBlobs()) {
                blobs[inc] = new Blob(blobGot.getId(),
                        blobGot.getMass(),
                        blobGot.getLocation().getX(),
                        blobGot.getLocation().getY());
                inc++;
            }

            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.getPlayers().contains(connection.getKey()) && connection.getValue().isOpen()) {
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
