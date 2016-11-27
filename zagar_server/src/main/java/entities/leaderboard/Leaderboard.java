package entities.leaderboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dao.Database;
import dao.UserDao;
import entities.user.UserEntity;
import javafx.collections.transformation.SortedList;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Егор on 27.11.2016.
 */
@Entity
@Table(name = "leaderboard")
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    @Column(name = "leaderboard_id")
    private Integer leaderboardID;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "leaderboard")
    @JsonIgnore
    private List<UserEntity> users;

    public Leaderboard() {
        this.users = new ArrayList<>();
    }

    public Leaderboard(@NotNull UserEntity newUser) {
        this.users = new ArrayList<>();
        this.users.add(newUser);
    }

    @NotNull
    public List<UserEntity> getUsers() {
        return this.users;
    }

    @NotNull
    public Integer getLeaderboardID() {
        return this.leaderboardID;
    }

    public boolean addUser(@NotNull UserEntity newUser) {
        Optional<UserEntity> userValid = this.users.stream()
                .filter(s -> s.getUserID().equals(newUser.getUserID())).findFirst();
        if  (userValid.isPresent()) {
            userValid.ifPresent(user -> {
                if (user.getLeaderboard() != null) {
                    user.getLeaderboard().deleteUser(newUser);
                }
                user.setLeaderboard(this);
                new UserDao().update(user);
            });
            this.users.add(newUser);
            return true;
        }
        return false;
    }

    public static Integer compareTo(UserEntity one, UserEntity two) {
        if (one.getScore() > two.getScore()) return 1;
        else if (one.getScore() < two.getScore()) return -1;
        return 0;
    }

    public boolean deleteUser(@NotNull UserEntity userToDelete) {
        Optional<UserEntity> userValid = this.users.stream()
                .filter(s -> s.getUserID().equals(userToDelete.getUserID())).findFirst();
        if  (userValid.isPresent()) {
            userValid.ifPresent(user -> {
                user.setLeaderboard(null);
                new UserDao().update(user);
            });
            this.users = this.users.stream()
                    .filter(user -> !user.getUserID().equals(userToDelete.getUserID())).collect(Collectors.toList());
            return true;
        }
        return false;
    }


    public boolean updateUser(@NotNull UserEntity userToUpdate, @NotNull Integer newScore) {
        Optional<UserEntity> userValid = this.users.stream()
                .filter(s -> s.getUserID().equals(userToUpdate.getUserID())).findFirst();
        if  (userValid.isPresent()) {
            userValid.ifPresent(user -> {
                user.updateScore(newScore);
                new UserDao().update(user);
            });
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "users = " + users +
                ", id = " + leaderboardID;
    }

}
