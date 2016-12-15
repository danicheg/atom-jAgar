package dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Database {

    private static final Logger LOG = LogManager.getLogger(Database.class);
    private static final SessionFactory SESSION_FACTORY;
    private static final String ERROR_MESSAGE = "Transaction failed.";

    static {
        SESSION_FACTORY = new Configuration().configure().buildSessionFactory();
        LOG.info("Session factory configured.");
    }

    private Database() {}

    static <T> List<T> selectTransactional(Function<Session, List<T>> selectAction) {
        Transaction txn = null;
        List<T> ts = Collections.emptyList();
        try (Session session = Database.openSession()) {
            txn = session.beginTransaction();
            ts = selectAction.apply(session);
            txn.commit();
        } catch (RuntimeException e) {
            LOG.error(ERROR_MESSAGE, e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
        return ts;
    }

    static void doTransactional(Function<Session, ?> f) {
        Transaction txn = null;
        try (Session session = Database.openSession()) {
            txn = session.beginTransaction();
            f.apply(session);
            txn.commit();
        } catch (RuntimeException e) {
            LOG.error(ERROR_MESSAGE, e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
    }

    static void doTransactional(List<Function<Session, ?>> tasks) {
        Transaction txn = null;
        try (Session session = Database.openSession()) {
            txn = session.beginTransaction();
            tasks.forEach(func -> func.apply(session));
            txn.commit();
        } catch (RuntimeException e) {
            LOG.error(ERROR_MESSAGE, e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
    }

    static void doTransactionalList(List<Consumer<Session>> tasks) {
        Transaction txn = null;
        try (Session session = Database.openSession()) {
            txn = session.beginTransaction();
            tasks.forEach(func -> func.accept(session));
            txn.commit();
        } catch (RuntimeException e) {
            LOG.error(ERROR_MESSAGE, e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
    }

    static void doTransactional(Consumer<Session> f) {
        Transaction txn = null;
        try (Session session = Database.openSession()) {
            txn = session.beginTransaction();
            f.accept(session);
            txn.commit();
        } catch (RuntimeException e) {
            LOG.error(ERROR_MESSAGE, e);
            if (txn != null && txn.isActive()) {
                txn.rollback();
            }
        }
    }

    public static Session openSession() {
        return SESSION_FACTORY.openSession();
    }

    public static void closeSession() {
        SESSION_FACTORY.close();
    }

}
