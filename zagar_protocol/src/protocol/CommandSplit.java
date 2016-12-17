package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandSplit extends Command {
    @NotNull
    public static final String NAME = "split";

    private String name;

    public CommandSplit(String name) {
        super(NAME);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
