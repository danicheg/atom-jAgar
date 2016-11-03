package zagar.network.handlers;

import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import protocol.CommandThankYou;
import zagar.Game;
import zagar.util.JSONDeserializationException;
import zagar.util.JSONHelper;
import zagar.util.Reporter;


public class PacketHandlerAuthOk {
  public PacketHandlerAuthOk(@NotNull String json) {
    CommandThankYou th;
    try {
      th = JSONHelper.fromJSON(json, CommandThankYou.class);
    } catch (JSONDeserializationException e) {
      e.printStackTrace();
      return;
    }
    Game.state = Game.GameState.AUTHORIZED;
    Response resp = new Response.Builder().

    Reporter.reportInfo(CommandThankYou.NAME, "Daniel");
  }
}
