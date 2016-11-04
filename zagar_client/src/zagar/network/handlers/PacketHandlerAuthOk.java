package zagar.network.handlers;

import org.jetbrains.annotations.NotNull;
import zagar.Game;

public class PacketHandlerAuthOk {
    public PacketHandlerAuthOk(@NotNull String json) {
        Game.state = Game.GameState.AUTHORIZED;
    }
}
