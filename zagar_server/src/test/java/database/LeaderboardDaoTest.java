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
    private UserEntity leaderboardTwoUser;

    private Leaderboard leaderboardOne;
    private Leaderboard leaderboardTwo;

    @Before
    public void setUp() {
        leaderboardDao = new LeaderboardDao();
        userDao = new UserDao();

        firstTestUser = new UserEntity("TestName", "TestPassword");
        leaderboardTwoUser = new UserEntity("user", "pass");

        leaderboardOne = new Leaderboard();
        leaderboardTwo = new Leaderboard(leaderboardTwoUser);
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

        try (Session session = Database.openSession()) {
            Transaction txn = session.beginTransaction();
            session.save(firstTestUser);
            leaderboardOne.addUser(firstTestUser);
            session.save(leaderboardOne);
            session.save(leaderboardTwo);
            leaderboardOne.addUser(leaderboardTwoUser);
            leaderboardTwoUser.setLeaderboard(leaderboardOne);
            session.save(leaderboardOne);
            session.save(leaderboardTwoUser);
            txn.commit();

            assertThat(leaderboardDao.getById(leaderboardOne.getLeaderboardID()).getUsers())
                    .containsExactly(firstTestUser, leaderboardTwoUser);

            assertThat(leaderboardDao.getById(leaderboardTwo.getLeaderboardID()))
                    .extracting(Leaderboard::getUsers)
                    .doesNotContain(leaderboardTwoUser);

            txn = session.beginTransaction();
            firstTestUser.setLeaderboard(null);
            leaderboardTwoUser.setLeaderboard(null);
            session.save(firstTestUser);
            session.save(leaderboardTwoUser);
            session.delete(leaderboardOne);
            session.delete(leaderboardTwo);
            session.delete(leaderboardTwoUser);
            session.delete(firstTestUser);
            txn.commit();
        }
    }

    @Test
    public void returnOnlyNLeadersFromLeaderboard() {

        try (Session session = Database.openSession()) {

            final int initialSize = leaderboardDao.getAll().size();

            Transaction txn = session.beginTransaction();
            leaderboardOne.addUser(firstTestUser);
            leaderboardOne.addUser(leaderboardTwoUser);
            session.save(firstTestUser);
            session.save(leaderboardTwoUser);
            session.save(leaderboardOne);
            txn.commit();

            assertThat(leaderboardDao.getNLeaders(leaderboardOne, 2))
                    .hasSize(initialSize + 2);

            txn = session.beginTransaction();
            firstTestUser.setLeaderboard(null);
            session.delete(leaderboardOne);
            session.delete(firstTestUser);
            session.delete(leaderboardTwoUser);
            txn.commit();

        }
    }
}
