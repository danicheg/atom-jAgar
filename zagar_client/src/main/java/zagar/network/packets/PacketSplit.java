package zagar.network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.CommandSplit;
import zagar.Game;
import zagar.util.JSONHelper;

import java.io.IOException;

public class PacketSplit {
    @NotNull
    private static final Logger log = LogManager.getLogger(">>>");

    private String name;

    public PacketSplit(String name) {
        this.name = name;
    }

    public void write() throws IOException {
        String msg = JSONHelper.toJSON(new CommandSplit(name));
        log.info("Sending [" + msg + "]");
        Game.socket.session.getRemote().sendString(msg);
    }
}
