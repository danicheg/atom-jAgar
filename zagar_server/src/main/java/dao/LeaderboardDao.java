package dao;

import entities.leaderboard.Leaderboard;
import entities.user.UserEntity;
import jersey.repackaged.com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LeaderboardDao implements Dao<Leaderboard> {

    private static final Logger log = LogManager.getLogger(UserEntity.class);

    @Override
    public List<Leaderboard> getAll() {
        log.info("All leaders successfully obtained from db");
        return Database.selectTransactional(session ->
                session.createQuery("from Leaderboard", Leaderboard.class).list());
    }


    @Override
    public List<Leaderboard> getAllWhere(String... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        return Database.selectTransactional(session ->
                session.createQuery("from Leaderboard where " + totalCondition, Leaderboard.class).list());
    }

    @Nullable
    public Leaderboard getById(Long id) {
        return getAllWhere("leaderboard_id = " + id).stream().findFirst().orElse(null);
    }

    public List<Leaderboard> getAllIn(String leaderboard) {
        log.info("All leaders in {} successfully obtained from db", leaderboard);
        return Database.selectTransactional(session ->
                session.createQuery("from Leaderboard lb " +
                        "inner join UserEntity ue " +
                        "on ue.leaderboard.leaderboardID = lb.leaderboardID " +
                        "where lb.leaderboardID = " + leaderboard, Leaderboard.class).list());
    }

    public List<UserEntity> getNLeaders(Leaderboard leaderboard, Integer amount) {
        Leaderboard lb = this.getById(leaderboard.getLeaderboardID());
        List<UserEntity> users = null;
        if (lb != null) {
            users = lb.getUsers().stream().sorted(UserEntity::compareTo).limit(amount).collect(Collectors.toList());
        }
        return users;
    }

    @Override
    public void insert(Leaderboard leaderboard) {
        Database.doTransactional((Function<Session, ?>) session -> session.save(leaderboard));
        log.info("Leaderboard {} inserted into db", leaderboard);
    }

    @Override
    public void insertAll(Leaderboard... leaderboards) {
        List<Leaderboard> listTokens = Arrays.asList(leaderboards);
        Stream<Function<Session, ?>> tasks = listTokens.parallelStream()
                .map(ldr -> session -> session.save(ldr));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        log.info("All leaderboards: '{}' inserted into DB", listTokens);
    }

    @Override
    public void update(Leaderboard leaderboard) {
        Database.doTransactional((Consumer<Session>) session -> session.update(leaderboard));
        log.info("Leaderboard {} successfully updated", leaderboard);
    }

    @Override
    public void delete(Leaderboard deleteLeaderboard) {
        Database.doTransactional(
                (Consumer<Session>) session -> session.delete(deleteLeaderboard)
        );
        log.info("Leaderboard '{}' was removed from DB", deleteLeaderboard);
    }

    //now works atomicity
    @Override
    public void deleteAll(Leaderboard... deleteLeaderboards) {
        List<Leaderboard> listTokens = Arrays.asList(deleteLeaderboards);
        Stream<Consumer<Session>> tasks = listTokens.parallelStream()
                .map(usr -> (Consumer<Session>) session -> session.delete(usr));
        Database.doTransactionalList(tasks.collect(Collectors.toList()));
        log.info("All leaderboards'{}' removed into DB", (Object[]) deleteLeaderboards);
    }

}
