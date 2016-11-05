package entities.token;

import entities.user.User;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "token")
public class Token {

    @Id
    @Column(name = "token")
    @NotNull
    private Long token;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @OneToOne
    @JoinColumn(name = "fk_user_id", referencedColumnName = "user_id")
    @NotNull
    private User user;

    /*
    Reason: ERROR [main] dao.Database (Database.java:34) - Transaction failed.
    javax.persistence.PersistenceException: org.hibernate.InstantiationException:
        No default constructor for entity:  : entities.user.User
    */
    public Token(){}

    public Token(@NotNull Long token, @NotNull User user) {
        this.token = token;
        date = LocalDate.now();
        this.user = user;
    }

    @NotNull
    public Long getToken() {
        return this.token;
    }

    public void setToken(@NotNull Long token) {
        this.token = token;
    }

    @NotNull
    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @NotNull
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || that.getClass() != getClass()) return false;
        if (this == that) return true;

        Token castToken = (Token) that;
        return token.equals(castToken.getToken());
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    @Override
    public String toString() {
        return "Token(" +
                "token=" + token +
                ')';
    }

}
