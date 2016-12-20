package network.handlers;

import dao.DatabaseAccessLayer;
import dao.TokenDao;
import dao.Validator;
import entities.token.Token;
import entities.user.UserEntity;
import main.ApplicationContext;
import matchmaker.MatchMaker;
import model.Player;
import network.ClientConnections;
import network.packets.PacketAuthFail;
import network.packets.PacketAuthOk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandAuth;
import utils.JSONDeserializationException;
import utils.JSONHelper;
import utils.RandomColorGenerator;

import java.io.IOException;

public class PacketHandlerAuth {

    private static final Logger LOG = LogManager.getLogger(PacketHandlerAuth.class);

    public PacketHandlerAuth(@NotNull Session session, @NotNull String json) throws Exception {

        CommandAuth commandAuth;

        try {
            commandAuth = JSONHelper.fromJSON(json, CommandAuth.class);
        } catch (JSONDeserializationException e) {
            LOG.error("CommandAuth - JSONDeserializationException: " + e);
            return;
        }
        if (!Validator.validateToken(commandAuth.getToken())) {

            try {
                new PacketAuthFail(
                        commandAuth.getLogin(), commandAuth.getToken(),
                        "Invalid user or password"
                ).write(session);
            } catch (IOException e) {
                LOG.error("Can't send PacketAuthFail because of: " + e);
            }

        } else {

            try {
                Player toRemovePlayer = Player.getPlayerByName(commandAuth.getLogin());
                if (toRemovePlayer != null) {
                    Player.removeUserFromSession(toRemovePlayer);
                }
                Player player = new Player(Player.idGenerator.next(), commandAuth.getLogin(),
                        RandomColorGenerator.generateRandomColor());
                UserEntity user = DatabaseAccessLayer.getUser(DatabaseAccessLayer.issueToken(commandAuth.getLogin()));
                player.setUser(user);
                ApplicationContext.instance().get(ClientConnections.class).registerConnection(player, session);
                new PacketAuthOk().write(session);
                ApplicationContext.instance().get(MatchMaker.class).joinGame(player);
            } catch (IOException e) {
                LOG.error("Can't send PacketAuthOk because of: " + e);
            }

        }
    }
}
