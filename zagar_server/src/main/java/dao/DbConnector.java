package dao;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {

    private static final Logger log = LogManager.getLogger(DbConnector.class);

    private static final String URL_TEMPLATE = "jdbc:h2:./%s";
    private static final String USER = "sa";
    private static final String PASSWORD = null;
    private static final String DB_NAME = "database/test";

    /*private static final String URL_TEMPLATE = "jdbx:postgresql://%s:%d/%s";
    private static final String USER = "postgres";
    private static final String PASSWORD = "StephenWilliamHawking";
    private static final String DB_NAME = "postgres";*/

    private static final String URL;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8080;


    static {
        try {
            Class.forName("org.h2.Driver");
            //Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Failed to load jdbc driver.", e);
            System.exit(-1);
        }

        URL = String.format(URL_TEMPLATE, DB_NAME);

        //URL = String.format(URL_TEMPLATE, HOST,PORT, DB_NAME);
        log.info("Success. DbConnector init.");

        try (Connection con = DbConnector.getConnection();
             Statement stm = con.createStatement()) {
            String query = "" +
                    "DROP TABLE IF EXISTS leaderboard;" +
                    "CREATE TABLE IF NOT EXISTS leaderboard (" +
                    "user_id bigint," +
                    "score int not null," +
                    "    CONSTRAINT FK_leader FOREIGN KEY (user_id) REFERENCES userentity(user_id)" +
                    ");";
            stm.execute(query);
            log.info("Successfully created DB");
        } catch (SQLException e) {
            log.error("Failed to create a table.", e);
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void initialise() {
        try (Connection con = DbConnector.getConnection();
             Statement stm = con.createStatement()) {
            String query = "" +
                    "DROP TABLE IF EXISTS leaderboard;" +
                    "CREATE TABLE IF NOT EXISTS leaderboard (" +
                        "user_id bigint," +
                        "score int not null," +
                    "    CONSTRAINT FK_leader FOREIGN KEY (user_id) REFERENCES userentity(user_id)" +
                    ");";
            stm.execute(query);
            log.info("Successfully created DB");
        } catch (SQLException e) {
            log.error("Failed to create a table.", e);
        }
    }

    private DbConnector() {}

}