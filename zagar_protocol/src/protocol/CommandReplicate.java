package protocol;

import org.jetbrains.annotations.NotNull;
import protocol.model.Blob;
import protocol.model.Cell;
import protocol.model.Food;
import protocol.model.Virus;

/**
 * @author apomosov
 */
public final class CommandReplicate extends Command {
    @NotNull
    public static final String NAME = "cells";
    @NotNull
    private final Food[] food;
    @NotNull
    private final Cell[] cells;
    @NotNull
    private final Virus[] viruses;
    @NotNull
    private final Blob[] blobs;

    public CommandReplicate(@NotNull Food[] food, @NotNull Cell[] cells, @NotNull Virus[] viruses, @NotNull Blob[] blobs) {
        super(NAME);
        this.food = food;
        this.cells = cells;
        this.viruses = viruses;
        this.blobs = blobs;
    }

    @NotNull
    public protocol.model.Cell[] getCells() {
        return cells;
    }

    @NotNull
    public Food[] getFood() {
        return food;
    }

    @NotNull
    public Virus[] getViruses() { return viruses; }

    @NotNull
    public Blob[] getBlobs() { return blobs; }
}
