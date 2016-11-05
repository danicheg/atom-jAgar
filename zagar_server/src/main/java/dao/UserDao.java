package dao;

import entities.user.User;
import jersey.repackaged.com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDao implements Dao<User> {

    private static final Logger log = LogManager.getLogger(User.class);

    @Override
    public List<User> getAll() {
        log.info("All users successfully obtained from db");
        return Database.selectTransactional(session ->
                session.createQuery("from User", User.class).list());
    }

    @Override
    public List<User> getAllWhere(String... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        return Database.selectTransactional(session ->
                session.createQuery("from User where " + totalCondition, User.class).list());
    }

    @Override
    public void insert(User user) {
        Database.doTransactional((Function<Session, ?>) session -> session.save(user));
        log.info("User {} inserted into db", user);
    }

    @Override
    public void insertAll(User... user) {
        List<User> listTokens = Arrays.asList(user);
        Stream<Function<Session, ?>> tasks = listTokens.parallelStream()
                .map(usr -> session -> session.save(usr));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        log.info("All tokens: '{}' inserted into DB", listTokens);
    }

    @Override
    public void delete(User deleteUser) {
        Database.doTransactional(
                (Consumer<Session>) session -> session.delete(deleteUser)
        );
        log.info("Token '{}' removed into DB", deleteUser.getName());
    }

    //now works atomicity
    @Override
    public void deleteAll(User... deleteUsers) {
        List<User> listTokens = Arrays.asList(deleteUsers);
        Stream<Consumer<Session>> tasks = listTokens.parallelStream()
                .map(usr -> (Consumer<Session>) session -> session.delete(usr));
        Database.doTransactionalList(tasks.collect(Collectors.toList()));
        log.info("All tokens '{}' removed into DB", deleteUsers);
    }

}
