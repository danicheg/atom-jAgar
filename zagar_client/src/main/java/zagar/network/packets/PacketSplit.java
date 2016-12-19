package zagar.network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import protocol.CommandSplit;
import zagar.Game;
import zagar.util.JSONHelper;

import java.io.IOException;

public class PacketSplit {

    private static final Logger LOG = LogManager.getLogger(PacketSplit.class);

    private String name;

    public PacketSplit(String name) {
        this.name = name;
    }

    public void write() throws IOException {
        String msg = JSONHelper.toJSON(new CommandSplit(name));
        LOG.info("Sending [" + msg + "]");
        Game.socket.session.getRemote().sendString(msg);
    }
    
}
