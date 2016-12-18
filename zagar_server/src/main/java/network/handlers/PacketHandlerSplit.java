package network.handlers;

import dao.DatabaseAccessLayer;
import main.ApplicationContext;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Message;
import messagesystem.MessageSystem;
import model.Cell;
import model.GameConstants;
import model.Location;
import model.Player;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandSplit;
import utils.JSONDeserializationException;
import utils.JSONHelper;

public class PacketHandlerSplit {

    private static final Logger LOG = LogManager.getLogger(PacketHandlerSplit.class);

    public PacketHandlerSplit(@NotNull Session session, @NotNull String json) {

        CommandSplit commandSplit;

        try {
            commandSplit = JSONHelper.fromJSON(json, CommandSplit.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }

        final MessageSystem messageSystem = ApplicationContext.instance().get(MessageSystem.class);
        final ClientConnectionServer client = messageSystem.getService(ClientConnectionServer.class);
        final Mechanics mechanicsService = messageSystem.getService(Mechanics.class);

        messageSystem.sendMessage(new Message(client.getAddress() , mechanicsService.getAddress()) {
            @Override
            public void exec(Abonent abonent) {
                LOG.info("Recieved command " + commandSplit.getCommand());
                String name = commandSplit.getName();
                mechanicsService.splitMove(name);
            }
        });

    }
}
