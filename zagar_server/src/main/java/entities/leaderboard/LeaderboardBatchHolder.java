package entities.leaderboard;


import com.google.gson.Gson;
import entities.user.User;
import entities.user.UserBatchHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardBatchHolder {

    private static final Gson gson = new Gson();

    private List<Leaderboard> leaders = new ArrayList<>();

    public LeaderboardBatchHolder(List<Leaderboard> users) {
        this.leaders = users;
    }

    public List<Leaderboard> getUsers() {
        return leaders;
    }

    public static LeaderboardBatchHolder readJson(String json) throws IOException {
        return gson.fromJson(json, LeaderboardBatchHolder.class);
    }

    public String writeJson() {
        return gson.toJson(this);
    }
}
