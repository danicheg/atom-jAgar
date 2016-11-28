package client;

import com.fasterxml.jackson.core.JsonProcessingException;
import dao.Database;
import dao.DatabaseAccessLayer;
import dao.LeaderboardDao;
import dao.UserDao;
import entities.leaderboard.LeaderBatchHolder;
import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test_client.RestClient;
import test_client.RestClientImpl;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LeaderboardTest {

    private static final RestClient CLIENT = new RestClientImpl();
    private static final String ALREADY_REGISTERED_USERNAME = "admin";
    private static final String ALREADY_REGISTERED_PASSWORD = "admin";
    private static final String ANOTHER_ALREADY_REGISTERED_USERNAME = "tester";
    private static final String ANOTHER_ALREADY_REGISTERED_PASSWORD = "tester";

    @Before
    public void start() throws Exception {
        CLIENT.register(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        CLIENT.register(ANOTHER_ALREADY_REGISTERED_USERNAME, ANOTHER_ALREADY_REGISTERED_PASSWORD);
    }

    @Test
    public void showNLeaderNames() {
        UserEntity firstTestUser = new UserEntity("TestName", "TestPassword");
        Leaderboard leaderboard = new Leaderboard(firstTestUser);
        CLIENT.register(firstTestUser.getName(), firstTestUser.getPassword());
        UserDao userDao = new UserDao();
        LeaderboardDao leaderboardDao = new LeaderboardDao();

        try (Session session = Database.openSession()) {
            Transaction transaction = session.beginTransaction();
            leaderboard.addUser(firstTestUser);
            userDao.insert(firstTestUser);
            leaderboardDao.insert(leaderboard);
            transaction.commit();
        }

        Long tokenOne = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME,ALREADY_REGISTERED_PASSWORD);
        Long tokenTwo = CLIENT.authenticateUser(firstTestUser.getName(),firstTestUser.getPassword());
        assertThat(CLIENT.getNLeaderNames(2,leaderboard)).contains(firstTestUser.getName());
        CLIENT.logoutPlayer(tokenOne);
        CLIENT.logoutPlayer(tokenTwo);
    }
}
