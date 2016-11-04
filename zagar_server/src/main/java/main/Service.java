package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public abstract class Service extends Thread {

    @NotNull
    private static final Logger log = LogManager.getLogger(Service.class);

    public Service(@NotNull String name) {
        super(name);
        if (log.isInfoEnabled()) {
            log.info("AccountServer thread [" + name + "] created");
        }
    }

}
