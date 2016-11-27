package network.handlers;

import main.ApplicationContext;
import mechanics.Mechanics;
import messageSystem.Abonent;
import messageSystem.Message;
import messageSystem.MessageSystem;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import utils.JSONDeserializationException;
import utils.JSONHelper;

public class PacketHandlerMove {

    @NotNull
    private final static Logger log = LogManager.getLogger(Mechanics.class);

    public PacketHandlerMove(@NotNull Session session, @NotNull String json) {

        CommandMove commandMove;
        try {
            commandMove = JSONHelper.fromJSON(json, CommandMove.class);
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
                log.info("Recieved command " + commandMove.getCommand());
            }
        });

    }
}
