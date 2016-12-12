package messagesystem;

import org.jetbrains.annotations.NotNull;

/**
 * @author e.shubin
 */
public final class Address {
    @NotNull
    private final String name;

    public Address(@NotNull String addressName) {
        name = addressName;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return '[' + name + ']';
    }
}
