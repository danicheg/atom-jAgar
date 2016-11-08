package dao;

import entities.user.UserEntity;
import jersey.repackaged.com.google.common.base.Joiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserDao implements Dao<UserEntity> {

    private static final Logger log = LogManager.getLogger(UserEntity.class);

    @Override
    public List<UserEntity> getAll() {
        log.info("All users successfully obtained from db");
        return Database.selectTransactional(session ->
                session.createQuery("from UserEntity", UserEntity.class).list());
    }

    public List<UserEntity> getAllLogin() {
        return Database.selectTransactional(session ->
                session.createQuery("SELECT u FROM UserEntity u " +
                        "INNER JOIN Token t " +
                        "ON t.user.userID = u.userID " +
                        "WHERE t.user is not null", UserEntity.class).list());
    }

    @Override
    public List<UserEntity> getAllWhere(String... conditions) {
        String totalCondition = Joiner.on(" and ").join(Arrays.asList(conditions));
        return Database.selectTransactional(session ->
                session.createQuery("from UserEntity where " + totalCondition, UserEntity.class).list());
    }

    @Override
    public void insert(UserEntity user) {
        Database.doTransactional((Function<Session, ?>) session -> session.save(user));
        log.info("UserEntity {} inserted into db", user);
    }

    @Override
    public void insertAll(UserEntity... user) {
        List<UserEntity> listTokens = Arrays.asList(user);
        Stream<Function<Session, ?>> tasks = listTokens.parallelStream()
                .map(usr -> session -> session.save(usr));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        log.info("All tokens: '{}' inserted into DB", listTokens);
    }

    @Override
    public void update(UserEntity user) {
        Database.doTransactional((Consumer<Session>) session -> session.update(user));
        log.info("UserEntity {} successfully updated", user);
    }

    @Override
    public void delete(UserEntity deleteUser) {
        Database.doTransactional(
                (Consumer<Session>) session -> session.delete(deleteUser)
        );
        log.info("Token '{}' removed into DB", deleteUser.getName());
    }

    //now works atomicity
    @Override
    public void deleteAll(UserEntity... deleteUsers) {
        List<UserEntity> listTokens = Arrays.asList(deleteUsers);
        Stream<Consumer<Session>> tasks = listTokens.parallelStream()
                .map(usr -> (Consumer<Session>) session -> session.delete(usr));
        Database.doTransactionalList(tasks.collect(Collectors.toList()));
        log.info("All tokens '{}' removed into DB", deleteUsers);
    }

}
