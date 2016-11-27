package database;


import dao.LeaderboardDao;
import dao.UserDao;
import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

public class LeaderboardDaoTest {

    private LeaderboardDao leaderboardDao;
    private UserDao userDao;

    private UserEntity firstTestUser;
    private UserEntity secondTestUser;

    private Leaderboard leaderboardOne;
    private Leaderboard leaderboardTwo;

    @Before
    public void setUp() {
        leaderboardDao = new LeaderboardDao();
        userDao = new UserDao();

        firstTestUser = new UserEntity("TestName", "TestPassword");
        secondTestUser = new UserEntity("user", "pass");

        leaderboardOne = new Leaderboard();
        leaderboardTwo = new Leaderboard(secondTestUser);
    }

    @Test
    public void getAllTest() {
        leaderboardDao.deleteAll();
        assertThat(leaderboardDao.getAll()).hasSize(0);
    }

    @Test
    public void insertLeaderboardTest() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.insert(leaderboardOne);
        assertThat(leaderboardDao.getAll())
                .hasSize(initialSize + 1);
        leaderboardDao.delete(leaderboardOne);
    }

    @Test
    public void updateLeaderTest() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.insert(leaderboardOne);
        leaderboardOne.addUser(firstTestUser);
        leaderboardDao.update(leaderboardOne);
        leaderboardOne.updateUser(firstTestUser,20);
        assertThat(leaderboardDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Leaderboard::getUsers)
                .hasSize(1);
        leaderboardDao.delete(leaderboardOne);
    }

    @Test
    public void checkLeaderboardHasUniqueMemberTest() {
        leaderboardOne.addUser(secondTestUser); //second leaderboard had this User before
        leaderboardDao.insert(leaderboardOne);
        Leaderboard lb = leaderboardDao.getById(leaderboardOne.getLeaderboardID());
        System.out.print(lb);
        if (lb != null) {
            List<UserEntity> users = lb.getUsers();
            System.out.print(users);
            assertThat(users).hasSize(2);
        }
        leaderboardDao.delete(leaderboardOne);
    }

    @Test
    public void NLeadersTest() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardOne.addUser(firstTestUser);
        leaderboardOne.addUser(secondTestUser);
        leaderboardDao.insert(leaderboardOne);
        assertThat(leaderboardDao.getNLeaders(leaderboardOne, 3)).hasSize(initialSize + 2);
        leaderboardDao.delete(leaderboardOne);
    }
}
