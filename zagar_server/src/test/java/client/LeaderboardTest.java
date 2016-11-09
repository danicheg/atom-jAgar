package client;

import entities.user.UserEntity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import test_client.RestClient;
import test_client.RestClientImpl;

import java.util.List;
import java.util.Random;

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
        Long tokenOne = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME,ALREADY_REGISTERED_PASSWORD);
        CLIENT.addScore(tokenOne, 20);
        assertThat(CLIENT.getNLeaderNames(2)).contains(ALREADY_REGISTERED_USERNAME);
    }
}
