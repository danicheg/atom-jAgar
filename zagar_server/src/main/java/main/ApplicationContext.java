package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://shipilev.net/blog/archive/safe-publication/
 *
 * @author apomosov
 */
public class ApplicationContext {

    private static final Logger LOG = LogManager.getLogger(ApplicationContext.class);
    @Nullable private static volatile ApplicationContext instance;
    @NotNull private final Map<Class, Object> contextMap = new ConcurrentHashMap<>();

    private ApplicationContext() {
        LOG.info(ApplicationContext.class.getName() + " initialized");
    }

    public static ApplicationContext instance() {
            synchronized (ApplicationContext.class) {
                if (instance == null) {
                    instance = new ApplicationContext();
                }
                return instance;
            }
    }

    public void put(@NotNull Class clazz, @NotNull Object object) {
        LOG.info("Put " + clazz);
        contextMap.put(clazz, object);
    }

    @NotNull
    public <T> T get(@NotNull Class<T> type) {
        return (T) contextMap.get(type);
    }
}