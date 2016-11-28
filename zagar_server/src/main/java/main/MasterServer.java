package main;

import accountserver.AccountServer;
import dao.Database;
import main.config.MasterServerConfiguration;
import mechanics.Mechanics;
import messageSystem.MessageSystem;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MasterServer {

    @NotNull
    private final static Logger log = LogManager.getLogger(MasterServer.class);

    public static void main(@NotNull String[] args) throws InterruptedException {
        Database.openSession();
        MasterServer server = new MasterServer();
        server.start();
    }

    private void start() throws InterruptedException {

        log.info("MasterServer started");

        for (Class service : MasterServerConfiguration.SERVICES_ARRAY) {
            try {
                final Class[] parentInterfaces = service.getInterfaces();
                if (parentInterfaces.length == 0) {
                    ApplicationContext.instance().put(service, service.newInstance());
                } else {
                    ApplicationContext.instance().put(service.getInterfaces()[0], service.newInstance());
                }
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("Can't create instance of class " + service);
            }
        }

        MessageSystem messageSystem = new MessageSystem();
        ApplicationContext.instance().put(MessageSystem.class, messageSystem);

        Mechanics mechanics = new Mechanics();

        messageSystem.registerService(Mechanics.class, mechanics);
        messageSystem.registerService(AccountServer.class,
                new AccountServer(MasterServerConfiguration.ACCOUNT_PORT));
        messageSystem.registerService(ClientConnectionServer.class,
                new ClientConnectionServer(MasterServerConfiguration.CLIENT_PORT));

        messageSystem.getServices().forEach(Service::start);

        for (Service service : messageSystem.getServices()) {
            service.join();
        }
    }

}
