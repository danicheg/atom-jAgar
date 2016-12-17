package network.handlers;

import dao.DatabaseAccessLayer;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Message;
import messagesystem.MessageSystem;
import model.Field;
import model.Food;
import model.GameConstants;
import model.GameSession;
import model.Location;
import model.Player;
import model.Vector;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import utils.JSONDeserializationException;
import utils.JSONHelper;

import java.util.Set;

public class PacketHandlerMove {

    private static final Logger LOG = LogManager.getLogger(PacketHandlerMove.class);

    public PacketHandlerMove(@NotNull Session session, @NotNull String json) {

        CommandMove commandMove;
        try {
            commandMove = JSONHelper.fromJSON(json, CommandMove.class);
        } catch (JSONDeserializationException e) {
            LOG.error("CommandMove - JSONDeserializationException: " + e);
            return;
        }

        final MessageSystem messageSystem = ApplicationContext.instance().get(MessageSystem.class);
        final ClientConnectionServer client = messageSystem.getService(ClientConnectionServer.class);
        final Mechanics mechanicsService = messageSystem.getService(Mechanics.class);

        messageSystem.sendMessage(new Message(client.getAddress(), mechanicsService.getAddress()) {
            @Override
            public void exec(Abonent abonent) {
                LOG.info("Recieved command " + commandMove.getCommand());
                float dx = commandMove.getDx();
                float dy = commandMove.getDy();
                String name = commandMove.getName();
                Player player = getPlayerByName(name);
                if (player != null) {
                    for (model.Cell cell : player.getCells()) {
                        float oldX = cell.getX();
                        float oldY = cell.getY();
                        int newX = Math.round(oldX + (dx - oldX) / cell.getRadius());
                        int newY = Math.round(oldY + (dy - oldY) / cell.getRadius());
                        if (Math.abs(newX) < GameConstants.FIELD_WIDTH && Math.abs(newY) < GameConstants.FIELD_HEIGHT) {
                            cell.setX(newX);
                            cell.setY(newY);
                            Set<Food> foods = player.getSession().getField().getFoods();
                            Location first = new Location(Math.round(oldX), Math.round(oldY));
                            Location second = new Location(Math.round(newX), Math.round(newY));
                            for (Food food : foods) {
                                float fX = food.getLocation().getX();
                                float fY = food.getLocation().getY();
                                Location food_center = new Location(Math.round(fX), Math.round(fY));
                                if (checkDistance(first, second, food_center, food.getMass(), cell.getMass())) {
                                    cell.setMass(cell.getMass() + food.getMass());
                                    player.getSession().getField().getFoods().remove(food);
                                    player.getUser().setScore(player.getScore());
                                    DatabaseAccessLayer.updateUser(player.getUser());
                                }
                            }
                        }
                    }
                }
            }
        });
        messageSystem.execForService(client);

    }

    private Player getPlayerByName(String name) {
        for (GameSession gameSession : ApplicationContext.instance().get(MatchMaker.class).getActiveGameSessions()) {
            for (Player player : gameSession.getPlayers()) {
                if (player.getName().equals(name)) {
                    return player;
                }
            }
        }
        return null;
    }

    private boolean checkDistance(Location first, Location second, Location foodCenter,
                                  float foodLength, float cellLength) {
        Vector vector = new Vector(second.getX() - first.getX(), second.getY() - first.getY());
        Vector normalVector = vector.makeNormal().normalize();
        Vector foodNormalVector = normalVector.extend(foodLength);
        Vector cellNormalVector = normalVector.extend(cellLength);

        Location edgeUp = foodNormalVector.getEnd(foodCenter);
        Location edgeDown = foodNormalVector.getStart(foodCenter);

        Location centerCellGone = cellNormalVector.intersectWith(vector, first, foodCenter);
        Location edgeUpCell = cellNormalVector.getEnd(centerCellGone);
        Location edgeDownCell = cellNormalVector.getStart(centerCellGone);

        float distanceOne = edgeDown.distanceTo(edgeUpCell);
        float distanceTwo = edgeUp.distanceTo(edgeDownCell);
        float length = 2 * cellNormalVector.length();
        return ((distanceOne < length) && (distanceTwo < length));
    }

}
