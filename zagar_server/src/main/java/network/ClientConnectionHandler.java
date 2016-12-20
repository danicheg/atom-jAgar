package network;

import com.google.gson.JsonObject;
import main.ApplicationContext;
import model.Player;
import network.handlers.PacketHandlerAuth;
import network.handlers.PacketHandlerEjectMass;
import network.handlers.PacketHandlerMove;
import network.handlers.PacketHandlerSplit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.jetbrains.annotations.NotNull;
import protocol.CommandAuth;
import protocol.CommandEjectMass;
import protocol.CommandMove;
import protocol.CommandSplit;
import utils.json.JSONHelper;

import java.util.Map;

public class ClientConnectionHandler extends WebSocketAdapter {

    private static final Logger LOG = LogManager.getLogger(ClientConnectionHandler.class);

    @Override
    public void onWebSocketConnect(@NotNull Session sess) {
        super.onWebSocketConnect(sess);
        LOG.info("Socket connected: " + sess);
    }

    @Override
    public void onWebSocketText(@NotNull String message) {
        super.onWebSocketText(message);
        LOG.info("Received packet: " + message);
        if (getSession().isOpen()) {
            handlePacket(message);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, @NotNull String reason) {
        super.onWebSocketClose(statusCode, reason);
        LOG.info("Socket closed: [" + statusCode + "] " + reason);
        ClientConnections clientConnections = ApplicationContext.instance().get(ClientConnections.class);
        for (Map.Entry<Player, Session> connection : clientConnections.getConnections()) {
            if (!connection.getValue().isOpen()) {
                clientConnections.removeConnection(connection.getKey());
            }
        }

    }

    @Override
    public void onWebSocketError(@NotNull Throwable cause) {
        super.onWebSocketError(cause);
        LOG.error("WebSocket error: " + cause);
    }

    private void handlePacket(@NotNull String msg) {
        JsonObject json = JSONHelper.getJSONObject(msg);
        String name = json.get("command").getAsString();
        switch (name) {
            case CommandAuth.NAME:
                try {
                    new PacketHandlerAuth(getSession(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CommandEjectMass.NAME:
                try {
                    new PacketHandlerEjectMass(getSession(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CommandMove.NAME:
                try {
                    new PacketHandlerMove(getSession(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case CommandSplit.NAME:
                try {
                    new PacketHandlerSplit(getSession(), msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                LOG.error("Not supported command received - " + name);
        }
    }

}
