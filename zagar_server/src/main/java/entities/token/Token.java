package entities.token;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    public Token(@NotNull Long token) {
        this.token = token;
        date = LocalDate.now();
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
