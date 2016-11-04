package entities.user;

import model.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "name"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @NotNull
    private static final Logger log = LogManager.getLogger(Player.class);

    @Id
    @GeneratedValue
    @Column(name = "userId", columnDefinition = "uuid")
    @NotNull
    private UUID userID;

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    @Column(name = "password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "email")
    @Nullable
    private String email;

    @Column(name = "registration_date")
    @NotNull
    private LocalDate registrationDate;

    /*
    Reason: ERROR [main] dao.Database (Database.java:34) - Transaction failed.
    javax.persistence.PersistenceException: org.hibernate.InstantiationException:
        No default constructor for entity:  : entities.user.User
    */
    public User() {}

    public User(@NotNull String name, @NotNull String password) {
        this.userID = UUID.randomUUID();
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

    public boolean checkPassword(@NotNull String pass) {
        return password.equals(pass);
    }

    @NotNull
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || that.getClass() != getClass()) return false;
        if (this == that) return true;
        User newUser = (User) that;
        return this.userID.equals(newUser.userID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userID);
    }

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
