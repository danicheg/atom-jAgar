package database;

import dao.Database;
import dao.LeaderboardDao;
import dao.UserDao;
import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    public void returnEmptyLeaderboardListOnStart() {
        assertThat(leaderboardDao.getAll()).hasSize(0);
    }

    @Test
    public void insertIntoLeaderboard() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.insert(leaderboardOne);
        assertThat(leaderboardDao.getAll())
                .hasSize(initialSize + 1);
        leaderboardDao.delete(leaderboardOne);
    }

    @Test
    public void deleteFromLeaderboard() {
        final int initialSize = leaderboardDao.getAll().size();
        leaderboardDao.insert(leaderboardOne);
        assertThat(leaderboardDao.getAll())
                .isNotEmpty()
                .hasSize(initialSize + 1);
        leaderboardDao.delete(leaderboardOne);
        assertThat(leaderboardDao.getAll())
                .isEmpty();
    }

    @Test
    @Parameters({"0", "12", "32", "432", "-321", "502", "-91", "-12"})
    public void updateLeaderboardWithDifferentUserScoreValue(int newScoreValue) {

        try (Session session = Database.openSession()) {

            final int initialSize = leaderboardDao.getAll().size();

            Transaction txn = session.beginTransaction();
            session.save(firstTestUser);
            leaderboardOne.addUser(firstTestUser);
            session.save(leaderboardOne);
            txn.commit();

            assertThat(leaderboardDao.getAll())
                    .isNotEmpty()
                    .hasSize(initialSize + 1)
                    .flatExtracting(Leaderboard::getUsers)
                    .hasSize(1)
                    .extracting(UserEntity::getScore)
                    .contains(16);

            txn = session.beginTransaction();
            final Integer oldScore = firstTestUser.getScore();
            firstTestUser.updateScore(newScoreValue);
            session.update(leaderboardOne);
            txn.commit();

            assertThat(leaderboardDao.getAll())
                    .flatExtracting(Leaderboard::getUsers)
                    .extracting(UserEntity::getScore)
                    .contains(oldScore + newScoreValue);

            txn = session.beginTransaction();
            firstTestUser.setLeaderboard(null);
            session.delete(leaderboardOne);
            session.delete(firstTestUser);
            txn.commit();

        }

    }

    @Test
    public void checkLeaderboardHasUniqueMember() {
        leaderboardDao.insert(leaderboardTwo);
        leaderboardDao.insert(leaderboardOne);
        leaderboardOne.addUser(secondTestUser);
        leaderboardDao.update(leaderboardOne);
        userDao.update(secondTestUser);
        assertThat(leaderboardDao.getById(leaderboardTwo.getLeaderboardID()))
                .extracting(Leaderboard::getUsers)
                .doesNotContain(secondTestUser);
        userDao.delete(secondTestUser);
        leaderboardDao.delete(leaderboardOne);
        leaderboardDao.delete(leaderboardTwo);
    }

    @Test
    public void returnOnlyNLeadersFromLeaderboard() {

        try (Session session = Database.openSession()) {

            final int initialSize = leaderboardDao.getAll().size();

            Transaction txn = session.beginTransaction();
            leaderboardOne.addUser(firstTestUser);
            leaderboardOne.addUser(secondTestUser);
            session.save(firstTestUser);
            session.save(secondTestUser);
            session.save(leaderboardOne);
            txn.commit();

            assertThat(leaderboardDao.getNLeaders(leaderboardOne, 2))
                    .hasSize(initialSize + 2);

            txn = session.beginTransaction();
            firstTestUser.setLeaderboard(null);
            session.delete(leaderboardOne);
            session.delete(firstTestUser);
            session.delete(secondTestUser);
            txn.commit();

        }
    }
}
