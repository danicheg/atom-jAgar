package zagar.network.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.CommandLeaderBoard;
import zagar.Game;
import zagar.util.JSONDeserializationException;
import zagar.util.JSONHelper;

public class PacketHandlerLeaderBoard {

    private static final Logger log = LogManager.getLogger(PacketHandlerLeaderBoard.class);

    public PacketHandlerLeaderBoard(@NotNull String json) {
        CommandLeaderBoard commandLeaderBoard;
        try {
            commandLeaderBoard = JSONHelper.fromJSON(json, CommandLeaderBoard.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }

        log.info("Get message {}", commandLeaderBoard);


        Game.leaderBoard = commandLeaderBoard.getLeaderBoard();
    }
}
