package replication;

import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.Field;
import model.GameSession;
import model.Player;
import model.PlayerCell;
import network.ClientConnections;
import network.packets.PacketLeaderBoard;
import network.packets.PacketReplicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import protocol.model.Cell;
import protocol.model.Food;
import protocol.model.Virus;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Alpi
 * @since 31.10.16
 */
public class FullStateReplicator implements Replicator {

    private static final Logger LOG = LogManager.getLogger(FullStateReplicator.class);

    @Override
    public void replicate() {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            Field field = gameSession.getField();
            Food[] food = new Food[field.getFoods().size()];
            int j = 0;
            for (model.Food food_got : field.getFoods()) {
                food[j] = new Food(food_got.getLocation().getX(),
                        food_got.getLocation().getY(),
                        food_got.getId(),
                        food_got.getColor().getRed(),
                        food_got.getColor().getGreen(),
                        food_got.getColor().getBlue());
                j++;
            }
            Virus[] viruses = new Virus[field.getViruses().size()];
            int k = 0;
            for (model.Virus virus_got : field.getViruses()) {
                viruses[k] = new Virus(virus_got.getId(),
                        virus_got.getMass(),
                        virus_got.getLocation().getX(),
                        virus_got.getLocation().getY());
                k++;
            }
            int numberOfCellsInSession = 0;
            for (Player player : gameSession.getPlayers()) {
                numberOfCellsInSession += player.getCells().size();
            }
            Cell[] cells = new Cell[numberOfCellsInSession];
            int i = 0;
            for (Player player : gameSession.getPlayers()) {
                for (PlayerCell playerCell : player.getCells()) {
                    cells[i] = new Cell(
                            playerCell.getId(),
                            player.getId(),
                            playerCell.getMass(),
                            playerCell.getX(),
                            playerCell.getY()
                    );
                    i++;
                }
            }

            String[] leaders = new String[10];
            List<Player> players = gameSession.getPlayers();
            if (players.size() < 10) {
                for (int z = 0; z < players.size(); z++) {
                    leaders[z] = players.get(z).getName();
                }
            } else {
                for (int z = 0; z < 10; z++) {
                    leaders[z] = players.get(z).getName();
                }
            }
            
            for (Map.Entry<Player, Session> connection
                    : ApplicationContext.instance().get(ClientConnections.class).getConnections()) {
                if (gameSession.getPlayers().contains(connection.getKey()) && connection.getValue().isOpen()) {
                    try {
                        new PacketReplicate(cells, food, viruses).write(connection.getValue());
                        new PacketLeaderBoard(leaders).write(connection.getValue());
                    } catch (IOException e) {
                        LOG.error("Exception in creating PacketReplicate: " + e);
                    }
                }
            }
        }

    /*ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions().stream().flatMap(
        gameSession -> gameSession.getPlayers().stream().flatMap(
            player -> player.getCells().stream()
        )
    ).map(playerCell -> new Cell(playerCell.getId(), ))*/
    }
}
