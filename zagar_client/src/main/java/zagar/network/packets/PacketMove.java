package zagar.network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import zagar.Game;
import zagar.util.JSONHelper;

import java.io.IOException;

public class PacketMove {

    @NotNull
    private static final Logger log = LogManager.getLogger(">>>");

    private float x;
    private float y;

    public PacketMove(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void write() throws IOException {
        String msg = JSONHelper.toJSON(new CommandMove(x, y));
        log.info("Sending [" + msg + "]");
        Game.socket.session.getRemote().sendString(msg);
    }
}
