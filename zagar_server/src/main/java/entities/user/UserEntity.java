package entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import entities.leaderboard.Leaderboard;
import entities.token.Token;
import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@Table(name = "userEntity", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "email")
})
public class UserEntity implements Comparable<UserEntity> {

    @NotNull
    private static final Logger log = LogManager.getLogger(Player.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    @JsonIgnore
    @NotNull
    private Long userID;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    @Nullable
    private Token token;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    @NotNull
    private String password;

    @Column(name = "email")
    @Nullable
    private String email;

    @Column(name = "registration_date")
    @JsonIgnore
    @NotNull
    private LocalDate registrationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_leaderboard_id")
    @JsonIgnore
    @Nullable
    private Leaderboard leaderboard;

    @Column(name = "score")
    @NotNull
    private Integer score = 16;

    /*
    Reason: ERROR [main] dao.Database (Database.java:34) - Transaction failed.
    javax.persistence.PersistenceException: org.hibernate.InstantiationException:
        No default constructor for entity:  : entities.user.UserEntity
    */
    public UserEntity() {
    }

    public UserEntity(@NotNull String name, @NotNull String password) {
        this.userID = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        this.name = name;
        this.password = password;
        registrationDate = LocalDate.now();
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

    @Nullable
    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    @Nullable
    public Token getToken() {
        return token;
    }

    public void setToken(@Nullable Token token) {
        this.token = token;
    }

    @Nullable
    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(@Nullable Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    @NotNull
    public Long getUserID() {
        return userID;
    }

    @NotNull
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    @NotNull
    public Integer getScore() {
        return this.score;
    }

    public void setScore(@NotNull Integer newScore) {
        this.score = newScore;
    }

    public void updateScore(@NotNull Integer newScore) {
        this.score += newScore;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || that.getClass() != getClass()) return false;
        if (this == that) return true;
        UserEntity newUser = (UserEntity) that;
        return this.userID.equals(newUser.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userID);
    }

    @Override
    public int compareTo(@NotNull UserEntity other) {
        return other.getScore() - this.score; // descending order
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                ", score=" + score +
                '}';
    }

}
