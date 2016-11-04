package database;

import dao.UserDao;
import entities.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

public class UserDaoTest {

    private UserDao userDao;
    private User testUser;

    @Before
    public void setUp() {
        userDao = new UserDao();
        testUser = new User("TestName", "TestPassword");
    }

    @Test
    public void getAllUsersTest() {
        assertThat(userDao.getAll())
                .hasSize(0);
    }

    @Test
    public void insertUserTest() {
        final int initialSize = userDao.getAll().size();
        userDao.insert(testUser);
        assertThat(userDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(User::getName, User::getRegistrationDate)
                .contains(tuple("TestName", LocalDate.now()));
    }

}
