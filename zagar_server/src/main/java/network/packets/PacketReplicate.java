package network.packets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import protocol.CommandReplicate;
import protocol.model.Blob;
import protocol.model.Cell;
import protocol.model.Food;
import protocol.model.Virus;
import utils.json.JSONHelper;

import java.io.IOException;

public class PacketReplicate {

    @NotNull
    private static final Logger log = LogManager.getLogger(PacketReplicate.class);
    @NotNull
    private final Cell[] cells;
    @NotNull
    private final Food[] food;
    @NotNull
    private final Virus[] viruses;

    @NotNull
    private final Blob[] blobs;

    public PacketReplicate(@NotNull Cell[] cells, @NotNull Food[] food, @NotNull Virus[] viruses, @NotNull Blob[] blobs) {
        this.cells = cells;
        this.food = food;
        this.viruses = viruses;
        this.blobs = blobs;
    }

    public void write(@NotNull Session session) throws IOException {
        String msg = JSONHelper.toJSON(new CommandReplicate(food, cells, viruses, blobs));
        log.info("Sending [" + msg + "]");
        session.getRemote().sendString(msg);
    }

}
