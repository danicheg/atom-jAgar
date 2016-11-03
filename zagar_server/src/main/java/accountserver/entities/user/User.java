package accountserver.entities.user;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class User {

    @NotNull
    private static final Logger log = LogManager.getLogger(Player.class);

    @NotNull transient private UUID userID;
    @NotNull private String name;
    @NotNull transient private String password;
    @Nullable transient private Player player;

    public User(@NotNull String name, @NotNull String password) {
        this.userID = UUID.randomUUID();
        this.name = name;
        this.password = password;
        if (log.isInfoEnabled()) {
            log.info(this + " created");
        }
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @Nullable
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(@Nullable Player player) {
        this.player = player;
    }

    public boolean checkPassword(@NotNull String pass) {
        return password.equals(pass);
    }

    @Override
    public String toString() {
        return "User(" +
                "userID=" + userID +
                ", name=" + name +
                ", password=" + password +
                ')';
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || that.getClass() != getClass()) return false;
        if (this == that) return true;

        User newUser = (User) that;
        return this.name.equals(newUser.name) &&
                this.userID.equals(newUser.userID) &&
                this.password.equals(newUser.password);
    }

    @Override
    public int hashCode() {
        int result = userID.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (player != null ? player.hashCode() : 0);
        return result;
    }

}