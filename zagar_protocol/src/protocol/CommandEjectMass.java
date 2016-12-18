package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandEjectMass extends Command {
    @NotNull
    public static final String NAME = "eject";

    private String name;

    public CommandEjectMass(String name) {
        super(NAME);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
