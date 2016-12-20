package zagar.network.handlers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import protocol.CommandReplicate;
import zagar.Game;
import zagar.util.JSONDeserializationException;
import zagar.util.JSONHelper;
import zagar.view.Blob;
import zagar.view.Cell;
import zagar.view.Food;
import zagar.view.Virus;

import java.util.Collections;

public class PacketHandlerReplicate {

    private static final Logger LOG = LogManager.getLogger(PacketHandlerReplicate.class);

    public PacketHandlerReplicate(@NotNull String json) {

        CommandReplicate commandReplicate;
        try {
            commandReplicate = JSONHelper.fromJSON(json, CommandReplicate.class);
        } catch (JSONDeserializationException e) {
            e.printStackTrace();
            return;
        }

        LOG.info("Get message {}", commandReplicate);

        Cell[] gameCells = new Cell[commandReplicate.getCells().length];
        for (int i = 0; i < commandReplicate.getCells().length; i++) {
            protocol.model.Cell c = commandReplicate.getCells()[i];
            gameCells[i] = new Cell(c.getX(), c.getY(), c.getSize(), c.getCellId(),
                    c.getName(), c.getColor().getRed(), c.getColor().getGreen(), c.getColor().getBlue());
        }

        Game.player.clear();
        Collections.addAll(Game.player, gameCells);
        Game.cells = gameCells;

        Virus[] gameViruses = new Virus[commandReplicate.getViruses().length];
        for (int i = 0; i < commandReplicate.getViruses().length; i++) {
            protocol.model.Virus v = commandReplicate.getViruses()[i];
            gameViruses[i] = new Virus(v.getX(), v.getY(), v.getSize(), v.getVirusId());
        }

        Game.viruses = gameViruses;

        Food[] gameFoods = new Food[commandReplicate.getFood().length];
        for (int i = 0; i < commandReplicate.getFood().length; i++) {
            protocol.model.Food f = commandReplicate.getFood()[i];
            gameFoods[i] = new Food(f.getX(), f.getY(), f.getId(), f.getR(), f.getG(), f.getB());
        }

        Game.foods = gameFoods;

        Blob[] gameBlobs = new Blob[commandReplicate.getBlobs().length];
        for (int i = 0; i < commandReplicate.getBlobs().length; i++) {
            protocol.model.Blob v = commandReplicate.getBlobs()[i];
            gameBlobs[i] = new Blob(v.getX(), v.getY(), v.getSize(), v.getVirusId());
        }

        Game.blobs = gameBlobs;

    }

}
