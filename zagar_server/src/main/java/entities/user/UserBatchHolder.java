package accountserver.entities.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserBatchHolder {

    private static final Gson gson = new Gson();

    /*static {
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }*/

    private List<User> users = new ArrayList<>();

    public UserBatchHolder(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public static UserBatchHolder readJson(String json) throws IOException {
        return gson.fromJson(json, UserBatchHolder.class);
    }

    public String writeJson() {
        return gson.toJson(this);
    }

}
