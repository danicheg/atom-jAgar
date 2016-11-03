package dao;

import entities.user.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class UserDao implements Dao {

    private static final Logger log = LogManager.getLogger(User.class);

    @Override
    public List getAll() {
        return Database.selectTransactional(session -> session.createQuery("from User").list());
    }
}
