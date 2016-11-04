package dao;

import entities.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserDao implements Dao<User> {

    private static final Logger log = LogManager.getLogger(User.class);

    @Override
    public List<User> getAll() {
        log.info("All users successfully obtained from db");
        return Database.selectTransactional(session -> session.createQuery("from User", User.class).list());
    }

    @Override
    public void insert(User user) {
        Database.doTransactional(session -> session.save(user));
        log.info("User {} inserted into db", user);
    }

}
