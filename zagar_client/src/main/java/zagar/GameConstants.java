package zagar;

public class GameConstants {

    public static final String DEFAULT_GAME_SERVER_HOST = "localhost";
    public static final int DEFAULT_GAME_SERVER_PORT = 7000;
    public static final String DEFAULT_ACCOUNT_SERVER_HOST = "localhost";
    public static final int DEFAULT_ACCOUNT_SERVER_PORT = 8080;
    public static final String DEFAULT_LOGIN = "zAgar";
    public static final String DEFAULT_PASSWORD = "pass";

    private GameConstants() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

}
