package client;

import org.junit.Before;
import org.junit.Test;
import test_client.RestClient;
import test_client.RestClientImpl;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AuthenticationTest {

    private static final RestClient CLIENT = new RestClientImpl();
    private static final String ALREADY_REGISTERED_USERNAME = "admin";
    private static final String ALREADY_REGISTERED_PASSWORD = "admin";
    private static final String ANOTHER_ALREADY_REGISTERED_USERNAME = "tester";
    private static final String ANOTHER_ALREADY_REGISTERED_PASSWORD = "tester";
    private static final String NOT_REGISTERED_USERNAME = "test";
    private static final String NOT_REGISTERED_PASSWORD = "test";
    private static final String PASSWORD = "test";
    private static final String USERNAME = "test";
    private static final String EMAIL = "test@test.test";
    private static final Long WRONG_TOKEN = new Random().nextLong();

    @Before
    public void start() throws Exception {
        CLIENT.register(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        CLIENT.register(ANOTHER_ALREADY_REGISTERED_USERNAME, ANOTHER_ALREADY_REGISTERED_PASSWORD);
    }

    @Test
    public void loginMustGenerateNotNullToken() throws Exception {
        Long rightToken =
                CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        assertNotNull(rightToken);
    }

    @Test
    public void loginMustGenerateDifferentTokensForDifferentUsers() {
        Long rightToken =
                CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        Long differentRightToken =
                CLIENT.authenticateUser(ANOTHER_ALREADY_REGISTERED_USERNAME, ANOTHER_ALREADY_REGISTERED_PASSWORD);
        assertNotEquals(rightToken, differentRightToken);
    }

    @Test
    public void loginMustGenerateSameTokenForSameUser() {
        Long rightToken =
                CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        Long sameRightToken =
                CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        assertEquals(rightToken, sameRightToken);
    }

    @Test
    public void changeName() {
        Long token = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        boolean result = CLIENT.changePlayerName(token, USERNAME);
        assertTrue(result);
    }

    @Test
    public void changePassword() {
        Long token = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        boolean result = CLIENT.changePlayerPassword(token, PASSWORD);
        assertTrue(result);
        CLIENT.changePlayerPassword(token, ALREADY_REGISTERED_PASSWORD);
    }

    @Test
    public void changeEmail() {
        Long token = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        boolean result = CLIENT.changePlayerEmail(token, EMAIL);
        assertTrue(result);
    }

    @Test
    public void logoutMustReturnTrueWithRightToken() {
        Long rightToken = CLIENT.authenticateUser(ALREADY_REGISTERED_USERNAME, ALREADY_REGISTERED_PASSWORD);
        boolean result = CLIENT.logoutPlayer(rightToken);
        assertTrue(result);
    }

    @Test
    public void logoutMustReturnFalseWithWrongToken() {
        assertFalse(CLIENT.logoutPlayer(WRONG_TOKEN));
    }

}
