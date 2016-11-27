package entities.leaderboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import entities.user.UserEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaderBatchHolder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    private List<UserEntity> leaders = new ArrayList<>();

    public LeaderBatchHolder(List<UserEntity> users) {
        this.leaders = users;
    }

    public LeaderBatchHolder() {
    }

    public static LeaderBatchHolder readJson(String json) throws IOException {
        return MAPPER.readValue(json, LeaderBatchHolder.class);
    }

    public static String writeJsonNames(List<String> m) throws JsonProcessingException {
        return MAPPER.writeValueAsString(m);
    }

    public static String[] readJsonNames(String m) throws IOException {
        return MAPPER.readValue(m, String[].class);
    }

    public List<UserEntity> getUsers() {
        return leaders;
    }

    public String writeJson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }
}
