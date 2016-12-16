package zagar.network.handlers;

import zagar.Game;
import zagar.Main;

public class PacketHandlerAuthOk {
    public PacketHandlerAuthOk() {
        Game.state = Game.GameState.AUTHORIZED;
    }
}
