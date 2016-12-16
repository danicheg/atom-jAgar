package network.handlers;

import com.google.gson.JsonObject;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Message;
import messagesystem.MessageSystem;
import model.Food;
import model.GameSession;
import model.GameUnit;
import model.Player;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import utils.JSONDeserializationException;
import utils.JSONHelper;

import java.util.HashSet;
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
                        cell.setX(Math.round(oldX + (dx - oldX)/cell.getRadius()));
                        cell.setY(Math.round(oldY + (dy - oldY)/cell.getRadius()));
                        Set<Food> foods = player.getSession().getField().getFoods();
                        for (Food food : foods) {
                            float fX = food.getLocation().getX();
                            float fY = food.getLocation().getY();
                            if (
                                    (fX < oldX
                                            &&
                                            fX > cell.getX()
                                    )
                                    ) {
                                float a = (cell.getY() - oldY) / (cell.getX() - oldX);
                                float b = (cell.getX() * oldY - cell.getY() * oldX) / (cell.getX() - oldX);
                                float fYf = a * fX + b;
                                if (
                                        (
                                                ((fY + food.getRadius()) <= fYf + cell.getRadius())
                                                        ||
                                                        (fYf + cell.getRadius() <= fYf - cell.getRadius())
                                        )
                                                &&
                                                (
                                                        ((fY - food.getRadius()) <= fYf + cell.getRadius())
                                                                ||
                                                                (fYf - cell.getRadius() <= fYf - cell.getRadius())
                                                )
                                        ) {
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
}
