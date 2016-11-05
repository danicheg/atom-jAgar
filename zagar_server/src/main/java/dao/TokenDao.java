package dao;

import entities.token.Token;
import jersey.repackaged.com.google.common.base.Joiner;
import org.hibernate.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenDao implements Dao<Token> {

    private static final Logger log = LogManager.getLogger(Token.class);

    @Override
    public List<Token> getAll() {
        List<Token> result = Database.selectTransactional(session ->
                session.createQuery("from Token", Token.class).list());
        log.info("All tokens successfully retrieved from DB: '{}'", result);
        return result;
    }

    @Override
    public List<Token> getAllWhere(String... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        List<Token> result = Database.selectTransactional(session ->
                session.createQuery("from Token where " + totalCondition, Token.class).list());
        log.info("Successfully retrieved tokens from DB: '{}' that satisfied conditions: '{}'",
                result, totalCondition);
        return result;
    }

    @Override
    public void insert(Token token) {
        Database.doTransactional(session -> session.save(token));
        log.info("Token '{}' inserted into DB", token);
    }

    @Override
    public void insertAll(Token... tokens) {
        List<Token> listTokens = Arrays.asList(tokens);
        Stream<Function<Session, ?>> tasks = listTokens.parallelStream()
                .map(tkn -> session -> session.save(tkn));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        log.info("All tokens: '{}' inserted into DB", listTokens);
    }

    @Override
    public void delete(Token deleteToken) {
        Database.doTransactional(
                session -> session.createQuery("delete Token where token = :delToken")
                        .setParameter("delToken", deleteToken.getToken())
                        .executeUpdate()
        );
        log.info("Token '{}' removed into DB", deleteToken);
    }

    //TODO: works not atomicity and it's suck.
    //TODO: We have List(Transaction1(delete(token1), ... TransactionN(delete(tokenN))), but must have:
    //TODO: Transaction(List(delete(token1), ... delete(tokenN))), as insertAll method
    @Override
    public void deleteAll(Token... deleteTokens) {
        List<Token> listTokens = Arrays.asList(deleteTokens);
        listTokens.forEach(tkn -> delete(tkn));
        log.info("All tokens '{}' removed into DB", deleteTokens);
    }
}
