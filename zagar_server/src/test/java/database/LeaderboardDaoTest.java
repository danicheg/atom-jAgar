package database;


import dao.LeaderboardDao;
import dao.TokenDao;
import dao.UserDao;
import entities.leaderboard.Leaderboard;
import entities.user.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.Assert.assertNotNull;

public class LeaderboardDaoTest {

    private LeaderboardDao leaderboardDao;
    private UserDao userDao;
    private TokenDao tokenDao;

    private User firstTestUser;
    private User secondTestUser;
    private User thirdTestUser;

    private Leaderboard firstTestLeader;
    private Leaderboard secondTestLeader;
    private Leaderboard thirdTestLeader;

    @Before
    public void setUp() {
        leaderboardDao = new LeaderboardDao();
        userDao = new UserDao();
        tokenDao = new TokenDao();

        firstTestUser = new User("TestName", "TestPassword");
        secondTestUser = new User("user", "pass");
        thirdTestUser = new User("Jegor", "dmonelove");

        firstTestLeader = new Leaderboard(firstTestUser.getUserID(),0);
        secondTestLeader = new Leaderboard(secondTestUser.getUserID(),0);
        thirdTestLeader = new Leaderboard(thirdTestUser.getUserID(),0);
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
    public void updateLeaderboardTest() {
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
    }

    @Test
    public void insertLeaderTest() {
        userDao.insert(firstTestUser);
        leaderboardDao.insert(firstTestLeader);
        assertNotNull(leaderboardDao.getAll().size());
        String query = String.format("user_id = '%s'",firstTestLeader.getUser());
        List<Leaderboard> list = leaderboardDao.getAllWhere(query);
        System.out.print(query);
        System.out.print(list);
    }
}
