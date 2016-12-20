package client;

import dao.Database;
import dao.LeaderboardDao;
import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import test_client.RestClient;
import test_client.RestClientImpl;

import static org.assertj.core.api.Assertions.assertThat;

public class LeaderboardTest {

    private static final RestClient CLIENT = new RestClientImpl();
    private final LeaderboardDao leaderboardDao = new LeaderboardDao();

    @Test
    public void showNLeaderReturnUsersInDescOrder() {

        UserEntity firstTestUser = new UserEntity("TestName", "TestPassword");
        UserEntity secondTestUser = new UserEntity("TestName2", "TestPassword2");
        Leaderboard leaderboard = new Leaderboard(firstTestUser);
        leaderboard.addUser(secondTestUser);
        CLIENT.register(firstTestUser.getName(), firstTestUser.getPassword());
        CLIENT.register(secondTestUser.getName(), secondTestUser.getPassword());
        firstTestUser.setLeaderboard(leaderboard);
        secondTestUser.setLeaderboard(leaderboard);

        Long tokenOne = CLIENT.authenticateUser(firstTestUser.getName(), firstTestUser.getPassword());
        Long tokenTwo = CLIENT.authenticateUser(secondTestUser.getName(), secondTestUser.getPassword());

        try (Session session = Database.openSession()) {
            final Transaction tx = session.beginTransaction();
            session.save(firstTestUser);
            session.save(secondTestUser);
            session.save(leaderboard);
            tx.commit();
        }

        assertThat(leaderboardDao.getNLeaders(leaderboard, 2))
                .isNotEmpty()
                .hasSize(2)
                .containsOnly(firstTestUser, secondTestUser);

        try (Session session = Database.openSession()) {
            final Transaction tx = session.beginTransaction();
            secondTestUser.setScore(33);
            session.update(secondTestUser);
            tx.commit();
        }

        assertThat(leaderboardDao.getNLeaders(leaderboard, 1))
                .isNotEmpty()
                .hasSize(1)
                .containsOnly(secondTestUser);

        CLIENT.logoutPlayer(tokenOne);
        CLIENT.logoutPlayer(tokenTwo);

    }

}
