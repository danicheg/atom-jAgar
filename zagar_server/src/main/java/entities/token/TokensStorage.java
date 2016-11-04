package entities.token;

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
    private static ConcurrentHashMap<User, Token> userTokens;
    private static ConcurrentHashMap<Token, User> usersTokensReversed;

    static {
        userTokens = new ConcurrentHashMap<>();
        usersTokensReversed = new ConcurrentHashMap<>();
    }

    @NotNull
    public static List<User> getUserList() {
        return new ArrayList<>(userTokens.keySet());
    }

    public static void remove(@NotNull Token token) {
        User user = usersTokensReversed.get(token);
        usersTokensReversed.remove(token);
        if (user != null) {
            userTokens.remove(user);
        }
    }

    public static boolean contains(@NotNull Token token) {
        return usersTokensReversed.containsKey(token);
    }

    public static Token issueToken(@NotNull String name) {

        User user = AuthenticationProvider.getRegisteredUsers().parallelStream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);

        Token token = getToken(user);
        if (token != null) {
            return token;
        }

        token = new Token(ThreadLocalRandom.current().nextLong(), user);
        log.info("Generate new token {} for User with name {}", token, name);
        TokensStorage.add(user, token);
        return token;

    }

    @NotNull
    public static Token parse(String rawToken) {
        Long longToken = Long.parseLong(rawToken.substring("Bearer".length()).trim());
        return usersTokensReversed.keySet().parallelStream()
                .filter(key -> key.getToken().equals(longToken))
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

    public static void add(@NotNull User user, @NotNull Token token) {
        userTokens.put(user, token);
        usersTokensReversed.put(token, user);
    }

    public static User getUser(@NotNull Token token) {
        return usersTokensReversed.get(token);
    }

    private static Token getToken(@NotNull User user) {
        return userTokens.get(user);
    }

}
