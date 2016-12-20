package dao;

import entities.token.Token;
import entities.user.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DatabaseAccessLayer {

    private static final Logger LOG = LogManager.getLogger(DatabaseAccessLayer.class);
    private static final String TOKEN_PREPEND = "token=";

    private static UserDao userDao;
    private static TokenDao tokenDao;

    static {
        userDao = new UserDao();
        tokenDao = new TokenDao();
    }

    private DatabaseAccessLayer() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

    @NotNull
    public static List<UserEntity> getLoginUserList() {
        return UserDao.getAllLoginUsers();
    }

    public static boolean contains(@NotNull Token token) {
        final String findByTokenCondition = TOKEN_PREPEND + token.getToken();
        return !(tokenDao.getAllWhere(findByTokenCondition).isEmpty());
    }

    public static Token issueToken(@NotNull String name) {
        final String findByNameCondition = "name=\'" + name + "\'";
        UserEntity user = userDao.getAllWhere(findByNameCondition).parallelStream()
                .findFirst()
                .orElse(null);

        Token token = getToken(user);
        if (token != null) {
            return token;
        }

        token = new Token(ThreadLocalRandom.current().nextLong(), user);
        tokenDao.insert(token);
        userDao.update(user);
        LOG.info("Generate new token {} for UserEntity with name {}", token, name);
        return token;
    }

    @NotNull
    public static Token parse(String rawToken) {
        Long longToken = Long.parseLong(rawToken.substring("Bearer".length()).trim());
        final String findByTokenCondition = TOKEN_PREPEND + longToken;
        return tokenDao.getAllWhere(findByTokenCondition).get(0);
    }

    public static UserEntity getUser(@NotNull Token token) {
        final String findByTokenCondition = TOKEN_PREPEND + token.getToken();
        return tokenDao.getAllWhere(findByTokenCondition).get(0).getUser();
    }

    private static Token getToken(@NotNull UserEntity user) {
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

    public static void updateUser(UserEntity user) {
        userDao.update(user);
    }

    public static void insertUser(UserEntity user) {
        userDao.insert(user);
    }

}
