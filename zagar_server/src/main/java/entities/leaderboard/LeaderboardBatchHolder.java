package entities.leaderboard;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardBatchHolder {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    private List<Leaderboard> leaders = new ArrayList<>();

    public LeaderboardBatchHolder(List<Leaderboard> users) {
        this.leaders = users;
    }

    public List<Leaderboard> getUsers() {
        return leaders;
    }

    public static LeaderboardBatchHolder readJson(String json) throws IOException {
        return MAPPER.readValue(json, LeaderboardBatchHolder.class);
    }

    public String writeJson() throws JsonProcessingException {
        return MAPPER.writeValueAsString(this);
    }

    public static String writeJsonNames(List<String> m) throws JsonProcessingException {
        return MAPPER.writeValueAsString(m);
    }
}
