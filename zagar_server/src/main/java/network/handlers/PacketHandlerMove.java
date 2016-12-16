package network.handlers;

import com.google.gson.JsonObject;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Message;
import messagesystem.MessageSystem;
import model.GameSession;
import model.Player;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import utils.JSONDeserializationException;
import utils.JSONHelper;

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
                        cell.setX(Math.round(cell.getX() + (dx - cell.getX())/cell.getRadius()));
                        cell.setY(Math.round(cell.getY() + (dy - cell.getY())/cell.getRadius()));
                    }
                }
            }
        });

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
