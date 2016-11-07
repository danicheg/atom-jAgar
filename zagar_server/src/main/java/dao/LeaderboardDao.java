package dao;

import entities.leaderboard.Leaderboard;
import jersey.repackaged.com.google.common.base.Joiner;
import jersey.repackaged.com.google.common.util.concurrent.ExecutionError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Constructor;
import java.sql.*;
import java.util.*;

import static jdk.nashorn.internal.runtime.PrototypeObject.getConstructor;

public class LeaderboardDao implements Dao<Leaderboard> {

    private static final Logger log = LogManager.getLogger(LeaderboardDao.class);

    private static final String SELECT_ALL_LEADERS =
            "SELECT * FROM leaderboard ORDER BY score DESC;";
    private static final String SELECT_N_LEADERS =
            "SELECT TOP(%d) * FROM leaderboard ORDER BY score DESC;";
    private static final String SELECT_LEADERS_WHERE =
            "SELECT * FROM leaderboard WHERE %s";
    private static final String UPDATE_LEADER_TEMPLATE =
            "UPDATE leaderboard SET score = ? WHERE user_id = ?";
    private static final String INSERT_LEADER_TEMPLATE =
            "INSERT INTO leaderboard (user_id, score) VALUES (?, ?)";
    private static final String DELETE_LEADER_TEMPLATE =
            "DELETE FROM leaderboard WHERE user_id = ?";
    private static final String DELETE_ALL_LEADERS =
            "DELETE FROM leaderboard";

    static {
        try (Connection con = DbConnector.getConnection();
             Statement stm = con.createStatement()) {
            //stm.execute("CREATE TABLE IF NOT EXISTS leaderboard (" +
            //        "user_id uuid UNIQUE," +
            //        "score int not null" +
            //        ")");
            String query = "DROP TABLE IF EXISTS leaderboard;" +
                    "SET FOREIGN_KEY_CHECKS = 0;" +
                    "CREATE TABLE IF NOT EXISTS leaderboard (" +
                        "user_id uuid," +
                        "score int not null," +
                    "    CONSTRAINT FK_leader FOREIGN KEY (user_id) REFERENCES user(user_id)" +
                    ");" +
                    "SET FOREIGN_KEY_CHECKS = 1;";
            stm.execute(query);
            //stm.execute("ALTER TABLE IF EXISTS leaderboard" +
             //       "        add constraint FK_leader" +
             //       "        foreign key if not exists (user_id)" +
             //       "        references user(user_id)");
        } catch (SQLException e) {
            log.error("Failed to create a table.", e);
        }
    }

    @Override
    public List<Leaderboard> getAll() {
        return getData(SELECT_ALL_LEADERS, null);
    }

    @Override
    public List<Leaderboard> getAllWhere(String ... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        log.info(totalCondition);
        return getData(SELECT_LEADERS_WHERE, totalCondition);
    }

    public List<Leaderboard> getNLeaders(Integer n) {
        return getData(SELECT_N_LEADERS, n);
    }

    @Override
    public void insert(Leaderboard leaderboard) {
        try (Connection con = DbConnector.getConnection();
             PreparedStatement stm = con.prepareStatement(INSERT_LEADER_TEMPLATE)) {
            stm.setInt(2, leaderboard.getScore());
            stm.setString(1, leaderboard.getUser().toString());
            stm.executeUpdate();
            //stm.execute(String.format(INSERT_LEADER_TEMPLATE, leaderboard.getUser(), leaderboard.getScore()));
        } catch (SQLException e) {
            log.error("Failed to insert.", e);
        }
    }

    @Override
    public void insertAll(Leaderboard... leaderboard) {
        for (Leaderboard elem : leaderboard) {
            insert(elem);
        }
    }

    @Override
    public void delete(Leaderboard leaderboard) {
        PreparedStatement stm = null;
        try (Connection con = DbConnector.getConnection()) {
            List<Leaderboard> leader = getData(SELECT_LEADERS_WHERE,String.format("user_id = '%s'", leaderboard.getUser()));
            if (leader.isEmpty()) {
                throw new Exception("There is no such leader!");
            } else {
                stm = con.prepareStatement(DELETE_LEADER_TEMPLATE);
                stm.setString(1,leaderboard.getUser().toString());
                Integer rowsUpdated = stm.executeUpdate();
                if (rowsUpdated != 1) {
                    throw new Exception("Nothing has been deleted!");
                }
            }
        } catch (SQLException e) {
            log.error("Failed to update.", e);
        } catch (Exception e) {
            log.error("Failed to update.", e);
        }
    }

    @Override
    public void deleteAll(Leaderboard... leaderboard) {
        try (Connection con = DbConnector.getConnection();
             Statement stm = con.createStatement()) {
            stm.execute(DELETE_ALL_LEADERS);
        } catch (SQLException e) {
            log.error("Failed to update.", e);
        }
    }

    @Override
    public void update(Leaderboard leaderboard) {
        PreparedStatement stm = null;
        try (Connection con = DbConnector.getConnection()) {
            List<Leaderboard> leader = getData(SELECT_LEADERS_WHERE,String.format("user_id = '%s'", leaderboard.getUser()));
            if (leader.isEmpty()) {
                stm = con.prepareStatement(INSERT_LEADER_TEMPLATE);
                stm.setInt(2, leaderboard.getScore());
                stm.setString(1, leaderboard.getUser().toString());
                log.info(stm);
                stm.executeUpdate();
            } else {
                stm = con.prepareStatement(UPDATE_LEADER_TEMPLATE);
                stm.setInt(1,leaderboard.getScore());
                stm.setString(2,leaderboard.getUser().toString());
                log.info(stm);
                Integer rowsUpdated = stm.executeUpdate();
                if (rowsUpdated != 1) {
                    throw new Exception("Nothing has been updated!");
                }
                //stm.execute(String.format(UPDATE_LEADER_TEMPLATE, leaderboard.getScore(),leaderboard.getUser()));
            }
        } catch (SQLException e) {
            log.error("Failed to update.", e);
        } catch (Exception e) {
            log.error("Failed to update.", e);
        }
    }

    @NotNull
    private static Leaderboard mapToLeaderboard(@NotNull ResultSet rs) throws SQLException {
        return new Leaderboard(UUID.fromString(rs.getString("user_id")), rs.getInt("score"));
    }

    private List<Leaderboard> getData(String query, Object input) {
        List<Leaderboard> leaders = new ArrayList<>();
        try (Connection con = DbConnector.getConnection();
             Statement stm = con.createStatement()) {
            ResultSet rs;
            if (input != null) {
                if (input instanceof String) {
                    String parameter = (String) input;
                    rs = stm.executeQuery(String.format(query, parameter));
                } else if (input instanceof Integer) {
                    Integer parameter = (Integer) input;
                    rs = stm.executeQuery(String.format(query, parameter));
                } else {
                    throw new Exception("The class of the input is not valid!");
                }
            } else {
                rs = stm.executeQuery(query);
            }
            if (rs != null) {
                while (rs.next()) {
                    leaders.add(mapToLeaderboard(rs));
                }
            }
        } catch (SQLException e) {
            log.error("Failed to get.", e);
            return Collections.emptyList();
        } catch (Exception e) {
            log.error("Failed to get.", e);
            return Collections.emptyList();
        }
        return leaders;
    }

}