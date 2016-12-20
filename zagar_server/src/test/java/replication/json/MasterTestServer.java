package replication.json;

import accountserver.AccountServer;
import main.ApplicationContext;
import main.Service;
import mechanics.Mechanics;
import messagesystem.MessageSystem;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class MasterTestServer {

    @NotNull
    private final static Logger log = LogManager.getLogger(MasterTestServer.class);

    public void start() throws InterruptedException {

        log.info("MasterTestServer started");

        for (Class service : MasterTestServerConfiguration.SERVICES_ARRAY) {
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
                new AccountServer(MasterTestServerConfiguration.ACCOUNT_PORT));
        messageSystem.registerService(ClientConnectionServer.class,
                new ClientConnectionServer(MasterTestServerConfiguration.CLIENT_PORT));

        messageSystem.getServices().forEach(Service::start);

        for (Service service : messageSystem.getServices()) {
            service.join();
        }
    }
}
