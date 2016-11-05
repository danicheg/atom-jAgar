package entities.token;

import dao.TokenDao;
import dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import accountserver.api.AuthenticationProvider;
import entities.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class TokensStorage {

    private static final Logger log = LogManager.getLogger(TokensStorage.class);
    private static UserDao userDao;
    private static TokenDao tokenDao;

    static {
        userDao = new UserDao();
        tokenDao = new TokenDao();
    }

    @NotNull
    public static List<User> getUserList() {
        return userDao.getAll();
    }

    public static void remove(@NotNull Token token) {
        tokenDao.delete(token);
    }

    public static boolean contains(@NotNull Token token) {
        final String findByTokenCondition = "token=\'" + token.getToken() + "\'";
        return !(tokenDao.getAllWhere(findByTokenCondition).isEmpty());
    }

    public static Token issueToken(@NotNull String name) {
        final String findByNameCondition = "name=\'" + name + "\'";
        User user = userDao.getAllWhere(findByNameCondition).parallelStream()
                .findFirst()
                .orElse(null);

        Token token = getToken(user);
        if (token != null) {
            return token;
        }

        token = new Token(ThreadLocalRandom.current().nextLong(), user);
        tokenDao.insert(token);
        log.info("Generate new token {} for User with name {}", token, name);
        return token;
    }

    @NotNull
    public static Token parse(String rawToken) {
        Long longToken = Long.parseLong(rawToken.substring("Bearer".length()).trim());
        return tokenDao.getAll().parallelStream()
                .filter(tkn -> tkn.getToken().equals(longToken))
                .findFirst()
                .get();
    }

    public static void validate(@NotNull String rawToken) throws Exception {
        Token token = parse(rawToken);
        if (!contains(token)) {
            throw new Exception("Token validation exception");
        }
        log.info("Correct token from '{}'", getUser(token).getName());
    }

    public static Boolean validateToken(@NotNull String rawToken) throws Exception {
        Token token = parse(rawToken);
        if (!contains(token)) {
            throw new Exception("Token validation exception");
        }
        log.info("Correct token from '{}'", getUser(token).getName());
        return true;
    }

    public static User getUser(@NotNull Token token) {
        return userDao.getAll().parallelStream()
                .filter(usr -> usr.getToken().equals(token))
                .findFirst()
                .orElse(null);
    }

    private static Token getToken(@NotNull User user) {
        return tokenDao.getAll().parallelStream()
                .filter(tkn -> tkn.getUser().equals(user))
                .findFirst()
                .orElse(null);
    }

}
