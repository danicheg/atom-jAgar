package network.handlers;

import main.ApplicationContext;
import matchmaker.MatchMaker;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Message;
import messagesystem.MessageSystem;
import model.*;
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

        messageSystem.sendMessage(new Message(client.getAddress() , mechanicsService.getAddress()) {
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
                        int newX = Math.round(oldX + (dx - oldX)/cell.getRadius());
                        int newY = Math.round(oldY + (dy - oldY)/cell.getRadius());
                        if (Math.abs(newX) < Field.getWidth() && Math.abs(newY) < Field.getHeight()) {
                            cell.setX(newX);
                            cell.setY(newY);
                        Set<Food> foods = player.getSession().getField().getFoods();
                            Location first = new Location(Math.round(oldX),Math.round(oldY));
                            Location second = new Location(Math.round(newX),Math.round(newY));
                        for (Food food : foods) {
                            float fX = food.getLocation().getX();
                            float fY = food.getLocation().getY();
                            Location food_center = new Location(Math.round(fX), Math.round(fY));
                                if (checkDistance(first, second, food_center, food.getMass(), cell.getMass())) {
                                    cell.setMass(cell.getMass() + food.getMass());
                                    player.getSession().getField().getFoods().remove(food);
                                }
                            }
                        }
                    }
                }
            }
        });
        //I can't say it improved the frequency of sending packet....
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

    private boolean checkDistance(Location first, Location second, Location center_food, float food_l, float cell_l) {
        Vector a = new Vector(second.getX() - first.getX(), second.getY() - first.getY());
        Vector b = a.makeNormal()
                .normalize();
        Vector c = b.extend(food_l);
        Vector d = b.extend(cell_l);

        Location edge_up = c.getEnd(center_food);
        Location edge_down = c.getStart(center_food);

        Location center_cell_gone = d.intersectWith(a, first, center_food);
        Location edge_up_cell = d.getEnd(center_cell_gone);
        Location edge_down_cell = d.getStart(center_cell_gone);

        float distanceOne = edge_down.distanceTo(edge_up_cell);
        float distanceTwo = edge_up.distanceTo(edge_down_cell);
        float length = 2 * d.length();
        return ((distanceOne <  length) && (distanceTwo <  length));
    }

    //        if (fX < oldX && fX > newX
//                ||
//                fX > oldX && fX < newX) {
//            float a = (newY - oldY) / (newX - oldX);
//            float b = (newX * oldY - newY * oldX) / (newX - oldX);
//            float fYf = a * fX + b;
//        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) *(y2 - y1));
}
