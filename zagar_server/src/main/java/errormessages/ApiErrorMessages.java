package errormessages;

public class ApiErrorMessages {

    public static final String WRONG_CREDENTIALS = "Wrong credentials.";
    public static final String CAN_NOT_FIND_TOKEN = "Can't find your token in database.";
    public static final String WRONG_NAME = "New name can't be null or empty.";
    public static final String WRONG_PASSWORD = "New password can't be null or empty.";
    public static final String WRONG_NAME_OR_PASSWORD = "Name or password can't be null or empty.";
    public static final String WRONG_EMAIL = "New email can't be null or empty.";
    public static final String BUSY_NAME = "Sorry, but user with this name is already present.";
    public static final String BUSY_EMAIL = "Sorry, but this email is already busy.";
    public static final String WRONG_LEADERBOARD = "Leaderboard is null.";
    public static final String WRONG_LEADERBOARD_ID = "Leaderboard ID is null.";

    private ApiErrorMessages() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

}
