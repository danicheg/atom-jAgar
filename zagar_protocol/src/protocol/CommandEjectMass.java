package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandEjectMass extends Command {
    @NotNull
    public static final String NAME = "eject";

    private String name;

    private float x;
    private float y;

    public CommandEjectMass(float x, float y, String name) {
        super(NAME);
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
