package entities.token;

import entities.user.UserEntity;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    private UserEntity user;

    public Token() {
        // need for hibernate
    }

    public Token(@NotNull Long token, @NotNull UserEntity user) {
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
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object that) {
        if (that == null || that.getClass() != getClass()) {
            return false;
        }
        if (this == that) {
            return true;
        }
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
