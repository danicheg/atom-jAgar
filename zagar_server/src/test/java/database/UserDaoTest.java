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
    private User firstTestUser;
    private User secondTestUser;
    private User thirdTestUser;

    @Before
    public void setUp() {
        userDao = new UserDao();
        firstTestUser = new User("TestName", "TestPassword");
        secondTestUser = new User("user", "pass");
        thirdTestUser = new User("Jegor", "dmonelove");
    }

    @Test
    public void getAllUsersTest() {
        assertThat(userDao.getAll()).hasSize(0);
    }

    @Test
    public void insertUserTest() {
        final int initialSize = userDao.getAll().size();
        userDao.insert(firstTestUser);
        assertThat(userDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(User::getName, User::getRegistrationDate)
                .contains(tuple("TestName", LocalDate.now()));
        userDao.delete(firstTestUser);
    }

    @Test
    public void deleteTest() {
        userDao.insert(secondTestUser);
        final int initialSize = userDao.getAll().size();
        userDao.delete(secondTestUser);
        assertThat(userDao.getAll()).hasSize(initialSize - 1);
    }

    @Test
    public void insertAllTest(){
        final int initialSize = userDao.getAll().size();
        userDao.insertAll(firstTestUser, secondTestUser, thirdTestUser);
        assertThat(userDao.getAll()).hasSize(initialSize + 3);
        userDao.deleteAll(firstTestUser, secondTestUser, thirdTestUser);
    }

    @Test
    public void deleteAllTest(){
        userDao.insertAll(firstTestUser, secondTestUser, thirdTestUser);
        final int initialSize = userDao.getAll().size();
        userDao.deleteAll(firstTestUser, secondTestUser, thirdTestUser);
        assertThat(userDao.getAll()).hasSize(initialSize - 3);
    }

}
