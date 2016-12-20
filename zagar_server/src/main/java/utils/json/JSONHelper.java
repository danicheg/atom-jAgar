package utils.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.jetbrains.annotations.NotNull;
import utils.json.JSONDeserializationException;

public class JSONHelper {

    private static Gson gson = new GsonBuilder().create();

    private JSONHelper() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

    @NotNull
    public static String toJSON(@NotNull Object object) {
        return gson.toJson(object);
    }

    @NotNull
    public static <T> T fromJSON(@NotNull String json, @NotNull Class<T> type) throws JSONDeserializationException {
        try {
            return gson.fromJson(json, type);
        } catch (JsonSyntaxException e) {
            throw new JSONDeserializationException(e);
        }
    }

    @NotNull
    public static JsonObject getJSONObject(@NotNull String string) {
        return gson.fromJson(string, JsonObject.class);
    }
}
