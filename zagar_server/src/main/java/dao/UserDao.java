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

    private static final Logger LOG = LogManager.getLogger(UserEntity.class);

    public static List<UserEntity> getAllLoginUsers() {
        return Database.selectTransactional(session ->
                session.createQuery("SELECT u FROM UserEntity u " +
                        "INNER JOIN Token t " +
                        "ON t.user.userID = u.userID ", UserEntity.class).list());
    }

    @Override
    public List<UserEntity> getAll() {
        LOG.info("All users successfully obtained from db");
        return Database.selectTransactional(session ->
                session.createQuery("from UserEntity", UserEntity.class).list());
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
        LOG.info("UserEntity {} inserted into db", user);
    }

    @Override
    public void insertAll(UserEntity... user) {
        List<UserEntity> listTokens = Arrays.asList(user);
        Stream<Function<Session, ?>> tasks = listTokens.stream()
                .map(usr -> session -> session.save(usr));
        Database.doTransactional(tasks.collect(Collectors.toList()));
        LOG.info("All tokens: '{}' inserted into DB", listTokens);
    }

    @Override
    public void update(UserEntity user) {
        Database.doTransactional((Consumer<Session>) session -> session.update(user));
        LOG.info("UserEntity {} successfully updated", user);
    }

    @Override
    public void delete(UserEntity deleteUser) {
        Database.doTransactional(
                (Consumer<Session>) session -> session.delete(deleteUser)
        );
        LOG.info("Token '{}' removed into DB", deleteUser.getName());
    }

    @Override
    public void deleteAll(UserEntity... deleteUsers) {
        List<UserEntity> listTokens = Arrays.asList(deleteUsers);
        Stream<Consumer<Session>> tasks = listTokens.stream()
                .map(usr -> (Consumer<Session>) session -> session.delete(usr));
        Database.doTransactionalList(tasks.collect(Collectors.toList()));
        LOG.info("All tokens '{}' removed into DB", (Object[]) deleteUsers);
    }

}
