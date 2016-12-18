package client;

import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Ignore;
import org.junit.Test;
import zagar.Game;
import zagar.auth.AuthClient;
import zagar.network.ServerConnectionSocket;
import zagar.network.packets.PacketEjectMass;
import zagar.network.packets.PacketMove;
import zagar.network.packets.PacketSplit;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static zagar.GameConstants.*;

public class SendingPacketsToServerTest {

    private String gameServerUrl = "ws://" + DEFAULT_GAME_SERVER_HOST + ":" + DEFAULT_GAME_SERVER_PORT;
    private AuthClient authClient = new AuthClient();

    @Test
    @Ignore
    public void SendPackets() throws IOException, InterruptedException {
        authClient.register("test", "test");
        Game.serverToken = "Bearer " + authClient.login("test", "test");
        Game.login = "test";
        Game.socket = new ServerConnectionSocket();
        final WebSocketClient client = new WebSocketClient();
        Thread thread = new Thread(() -> {
            try {
                client.start();
                URI serverURI = new URI(gameServerUrl + "/clientConnection");
                ClientUpgradeRequest request = new ClientUpgradeRequest();
                request.setHeader("Origin", "zagar.io");
                client.connect(Game.socket, serverURI, request);
                Game.socket.awaitClose(7, TimeUnit.DAYS);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
        thread.start();
        while (Game.socket == null || Game.socket.session == null || !Game.socket.session.isOpen()) {
            new PacketEjectMass(Game.followX, Game.followY, Game.login).write();
            new PacketSplit(Game.login).write();
            new PacketMove(13.4f, 1.5f, Game.login).write();
        }
    }

}
