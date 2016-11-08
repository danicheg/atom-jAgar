package database;


import dao.LeaderboardDao;
import dao.UserDao;
import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import org.junit.Before;
import org.junit.Test;

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

    private Leaderboard firstTestLeader;
    private Leaderboard secondTestLeader;

    @Before
    public void setUp() {
        leaderboardDao = new LeaderboardDao();
        userDao = new UserDao();

        firstTestUser = new UserEntity("TestName", "TestPassword");
        secondTestUser = new UserEntity("user", "pass");

        firstTestLeader = new Leaderboard(firstTestUser.getUserID(),0);
        secondTestLeader = new Leaderboard(secondTestUser.getUserID(),0);
    }

    @Test
    public void getAllTest() {
        leaderboardDao.deleteAll();
        assertThat(leaderboardDao.getAll()).hasSize(0);
    }

    @Test
    public void insertUserTest() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.insert(firstTestLeader);
        assertThat(leaderboardDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Leaderboard::getUser, Leaderboard::getScore)
                .contains(tuple(firstTestUser.getUserID(), 0));
        leaderboardDao.delete(firstTestLeader);
    }

    @Test
    public void updateLeaderTest() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.update(firstTestLeader);
        Integer oldMark = firstTestLeader.getScore();
        Integer newMark = oldMark + 20;
        firstTestLeader.addScore(20);
        leaderboardDao.update(firstTestLeader);
        assertThat(leaderboardDao.getAll())
                .hasSize(initialSize + 1)
                .extracting(Leaderboard::getUser, Leaderboard::getScore)
                .contains(tuple(firstTestUser.getUserID(), newMark));
        leaderboardDao.delete(firstTestLeader);
    }

    @Test
    public void insertLeaderTest() {
        userDao.insert(firstTestUser);
        leaderboardDao.insert(firstTestLeader);
        assertNotNull(leaderboardDao.getAll().size());
        assertEquals(
                firstTestLeader,
                leaderboardDao.getAllWhere(String.format("user_id = %s",firstTestLeader.getUser()))
                .stream()
                .findFirst()
                .orElse(null)
                );
        leaderboardDao.delete(firstTestLeader);
        userDao.delete(firstTestUser);
    }

    @Test
    public void compareTwoLeadersTest() {
        userDao.insert(firstTestUser);
        userDao.insert(secondTestUser);
        leaderboardDao.update(firstTestLeader);
        leaderboardDao.update(secondTestLeader);
        assertNotEquals(
                secondTestLeader,
                leaderboardDao.getAllWhere(String.format("user_id = %s",firstTestLeader.getUser()))
                        .stream()
                        .findFirst()
                        .orElse(null)
        );
        leaderboardDao.delete(firstTestLeader);
        leaderboardDao.delete(secondTestLeader);
        userDao.delete(firstTestUser);
        userDao.delete(secondTestUser);
    }
}
