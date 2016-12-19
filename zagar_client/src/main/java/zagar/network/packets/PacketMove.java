package zagar.network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.CommandMove;
import zagar.Game;
import zagar.util.JSONHelper;

import java.io.IOException;

public class PacketMove {

    private static final Logger LOG = LogManager.getLogger(PacketMove.class);

    private float x;
    private float y;
    private String name;

    public PacketMove(float x, float y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void write() throws IOException {
        String msg = JSONHelper.toJSON(new CommandMove(x, y, name));
        LOG.info("Sending [" + msg + "]");
        Game.socket.session.getRemote().sendString(msg);
    }

}
