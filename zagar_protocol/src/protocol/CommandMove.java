package protocol;

import org.jetbrains.annotations.NotNull;

/**
 * @author apomosov
 */
public final class CommandMove extends Command {
    @NotNull
    public static final String NAME = "move";

    private final float dx;
    private final float dy;
    private final String name;

    public CommandMove(float dx, float dy, String name) {
        super(NAME);
        this.dx = dx;
        this.dy = dy;
        this.name = name;
    }

    public float getDx() {
        return dx;
    }

    public float getDy() {
        return dy;
    }

    public String getName() {
        return name;
    }
}
