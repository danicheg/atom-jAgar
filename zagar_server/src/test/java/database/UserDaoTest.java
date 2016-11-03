package database;

import dao.UserDao;
import org.junit.Test;

public class UserDaoTest {

    UserDao userDao = new UserDao();

    @Test
    public void getAllTest() {
        System.out.println(userDao.getAll());
    }

}
