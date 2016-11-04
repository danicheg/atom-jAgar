package dao;

import entities.user.User;
import jersey.repackaged.com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;

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
        Database.doTransactional(session -> session.save(user));
        log.info("User {} inserted into db", user);
    }

}
