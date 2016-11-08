package entities.leaderboard;

import java.util.UUID;

public class Leaderboard {

    private Long user_id;
    private Integer score;

    public Leaderboard(Long user_id, Integer score) {
        this.user_id = user_id;
        this.score = score;
    }

    public Long getUser() {
        return this.user_id;
    }

    public Integer getScore() {
        return this.score;
    }

    public void addScore(Integer newScore) {
        this.score = this.score + newScore;
    }

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != Leaderboard.class) return false;
        Leaderboard leader = (Leaderboard) object;
        if (this.user_id.equals(leader.user_id) && this.score == leader.score) return true;
        return false;
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "user=" + this.user_id +
                ", score=" + this.score +
                '}';
    }
}
