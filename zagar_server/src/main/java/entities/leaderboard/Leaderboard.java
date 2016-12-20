package entities.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entities.user.UserEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    @Column(name = "leaderboard_id")
    private Long leaderboardID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "leaderboard", cascade = CascadeType.PERSIST)
    private List<UserEntity> users = new ArrayList<>();

    public Leaderboard() {
        //need for hibernate
    }

    public Leaderboard(@NotNull UserEntity newUser) {
        this.users.add(newUser);
    }

    @NotNull
    public List<UserEntity> getUsers() {
        return this.users;
    }

    @NotNull
    public Long getLeaderboardID() {
        return this.leaderboardID;
    }

    public void addUser(@NotNull UserEntity newUser) {
        users.add(newUser);
        newUser.setLeaderboard(this);
    }

    public void deleteUser(@NotNull UserEntity deleteUser) {
        users.remove(deleteUser);
        deleteUser.setLeaderboard(null);
    }

    @Override
    public String toString() {
        return "Leaderboard{" +
                "leaderboardID=" + leaderboardID +
                ", users=" + users +
                '}';
    }
}
