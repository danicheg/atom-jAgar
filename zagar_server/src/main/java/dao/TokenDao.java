package dao;

import entities.token.Token;
import jersey.repackaged.com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenDao implements Dao<Token> {

    private static final Logger LOG = LogManager.getLogger(Token.class);

    @Override
    public List<Token> getAll() {
        List<Token> result = Database.selectTransactional(session ->
                session.createQuery("from Token", Token.class).list());
        LOG.info("All tokens successfully retrieved from DB: '{}'", result);
        return result;
    }

    @Override
    public List<Token> getAllWhere(String... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        final List<Token> result = Database.selectTransactional(session ->
                session.createQuery("from Token where " + totalCondition, Token.class).list());
        LOG.info("Successfully retrieved tokens from DB: '{}' that satisfied conditions: '{}'",
                result, totalCondition);
        return result;
    }

    @Override
    public void insert(Token token) {
        token.getUser().setToken(token);
        Database.doTransactional((Function<Session, ?>) session -> session.save(token));
        LOG.info("Token '{}' inserted into DB", token);
    }

    @Override
    public void insertAll(Token... tokens) {
        List<Token> listTokens = Arrays.asList(tokens);
        listTokens.forEach(tkn -> tkn.getUser().setToken(tkn));
        Stream<Function<Session, ?>> tasks = listTokens.stream()
                .map(tkn -> session -> session.save(tkn));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        LOG.info("All tokens: '{}' inserted into DB", listTokens);
    }

    @Override
    public void update(Token token) {
        Database.doTransactional((Consumer<Session>) session -> session.update(token));
        LOG.info("Token '{}' successfully updated", token);
    }

    @Override
    public void delete(Token deleteToken) {
        Database.doTransactional((Consumer<Session>) session -> session.delete(deleteToken));
        LOG.info("Token '{}' removed into DB", deleteToken);
    }

    @Override
    public void deleteAll(Token... deleteTokens) {
        List<Token> listTokens = Arrays.asList(deleteTokens);
        Stream<Consumer<Session>> tasks = listTokens.stream()
                .map(tkn -> (Consumer<Session>) session -> session.delete(tkn));
        Database.doTransactionalList(tasks.collect(Collectors.toList()));
        LOG.info("All tokens '{}' removed from DB", listTokens);
    }

}
