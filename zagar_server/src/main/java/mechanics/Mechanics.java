package mechanics;

import main.ApplicationContext;
import main.Service;
import messagesystem.Message;
import messagesystem.MessageSystem;
import messagesystem.messages.ReplicateMsg;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import ticker.Tickable;
import ticker.Ticker;

public class Mechanics extends Service implements Tickable {

    private static final Logger log = LogManager.getLogger(Mechanics.class);

    public Mechanics() {
        super("mechanics");
    }

    @Override
    public void run() {
        log.info(getAddress() + " started");
        Ticker ticker = new Ticker(this, 1);
        ticker.loop();
    }

    @Override
    public void tick(long elapsedNanos) {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            log.error(e);
            Thread.currentThread().interrupt();
        }

        log.info("Start replication");
        @NotNull MessageSystem messageSystem = ApplicationContext.instance().get(MessageSystem.class);
        Message message = new ReplicateMsg(this.getAddress());
        messageSystem.sendMessage(message);

        //execute all messages from queue
        messageSystem.execForService(this);
    }

}
