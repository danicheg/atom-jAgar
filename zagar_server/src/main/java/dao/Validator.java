package dao;

import entities.token.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class Validator {

    private static final Logger LOG = LogManager.getLogger(Validator.class);

    public static void validate(@NotNull String rawToken) throws Exception {
        Token token = DatabaseAccessLayer.parse(rawToken);
        if (!DatabaseAccessLayer.contains(token)) {
            throw new Exception("Token validation exception");
        }
        LOG.info("Correct token from '{}'", DatabaseAccessLayer.getUser(token).getName());
    }

    public static Boolean validateToken(@NotNull String rawToken) throws Exception {
        Token token = DatabaseAccessLayer.parse(rawToken);
        if (!DatabaseAccessLayer.contains(token)) {
            throw new Exception("Token validation exception");
        }
        LOG.info("Correct token from '{}'", DatabaseAccessLayer.getUser(token).getName());
        return true;
    }
}
