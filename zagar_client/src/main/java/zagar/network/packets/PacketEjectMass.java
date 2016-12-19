package zagar.network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.CommandEjectMass;
import zagar.Game;
import zagar.util.JSONHelper;

import java.io.IOException;

public class PacketEjectMass {

    private static final Logger LOG = LogManager.getLogger(PacketEjectMass.class);

    private String name;

    private float x;
    private float y;

    public PacketEjectMass(float x, float y, String name) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void write() throws IOException {
        String msg = JSONHelper.toJSON(new CommandEjectMass(x,y, name));
        LOG.info("Sending [" + msg + "]");
        Game.socket.session.getRemote().sendString(msg);
    }

}
