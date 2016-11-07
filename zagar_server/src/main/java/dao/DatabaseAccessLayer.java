package dao;

import entities.token.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import entities.user.User;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DatabaseAccessLayer {

    private static final Logger log = LogManager.getLogger(DatabaseAccessLayer.class);
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


    public static boolean contains(@NotNull Token token) {
        final String findByTokenCondition = "token=" + token.getToken();
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
        userDao.update(user);
        log.info("Generate new token {} for User with name {}", token, name);
        return token;
    }

    @NotNull
    public static Token parse(String rawToken) {
        Long longToken = Long.parseLong(rawToken.substring("Bearer".length()).trim());
        final String findByTokenCondition = "token=" + longToken;
        /*final Token result = tokenDao.getAllWhere(findByTokenCondition).get(0);
        log.info("Result of finding token: {}", result);*/
        return tokenDao.getAllWhere(findByTokenCondition).get(0);
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
        final String findByTokenCondition = "token=" + token.getToken();
        return tokenDao.getAllWhere(findByTokenCondition).get(0).getUser();
    }

    private static Token getToken(@NotNull User user) {
        final String findByNameCondition = "name=\'" + user.getName() + "\'";
        return userDao.getAllWhere(findByNameCondition).get(0).getToken();
    }

    public static Boolean checkByCondition(String... conditions) {
        return userDao.getAllWhere(conditions)
                .stream()
                .findFirst()
                .isPresent();
    }

    public static void removeToken(Token token) {
        tokenDao.delete(token);
    }

    public static void updateUser(User user) {
        userDao.update(user);
    }

    public static void insertUser(User user) {
        userDao.insert(user);
    }

}