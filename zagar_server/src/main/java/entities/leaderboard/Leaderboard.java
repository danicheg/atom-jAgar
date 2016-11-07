package entities.leaderboard;

import entities.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class Leaderboard {

    private UUID user_id;
    private Integer score;

    public Leaderboard(UUID user_id, Integer score) {
        this.user_id = user_id;
        this.score = score;
    }

    public UUID getUser() {
        return this.user_id;
    }

    public Integer getScore() {
        return this.score;
    }

    public void addScore(Integer newScore) {
        this.score = this.score + newScore;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "user=" + this.user_id +
                ", score=" + this.score +
                '}';
    }
}
