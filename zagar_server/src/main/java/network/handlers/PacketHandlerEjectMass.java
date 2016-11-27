package network.handlers;

import main.ApplicationContext;
import mechanics.Mechanics;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import network.ClientConnections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandEjectMass;
import utils.JSONDeserializationException;
import utils.JSONHelper;

public class PacketHandlerEjectMass {

    @NotNull
    private final static Logger log = LogManager.getLogger(Mechanics.class);

    public PacketHandlerEjectMass(@NotNull Session session, @NotNull String json) {

        CommandEjectMass commandEjectMass;

        try {
            commandEjectMass = JSONHelper.fromJSON(json, CommandEjectMass.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }

        final MessageSystem messageSystem = ApplicationContext.instance().get(MessageSystem.class);
        final ClientConnections clientConnections = ApplicationContext.instance().get(ClientConnections.class);
        final Mechanics mechanicsService = messageSystem.getService(Mechanics.class);

        messageSystem.sendMessage(new Message(
                new Address(clientConnections.toString()),
                new Address(mechanicsService.toString())
        ) {
            @Override
            public void exec(Abonent abonent) {
                log.info(commandEjectMass);
            }
        });

    }
}
