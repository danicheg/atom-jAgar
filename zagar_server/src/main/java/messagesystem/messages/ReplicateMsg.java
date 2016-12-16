package messagesystem.messages;

import main.ApplicationContext;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;
import messagesystem.MessageSystem;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import replication.FullStateReplicator;
import replication.Replicator;

public class ReplicateMsg extends Message {

    @NotNull
    private static final Logger log = LogManager.getLogger(ReplicateMsg.class);

    public ReplicateMsg(Address from) {
        super(from, ApplicationContext.instance().get(MessageSystem.class).getService(
                Mechanics.class).getAddress()
        );
    }

    @Override
    public void exec(Abonent abonent) {
        log.info("ReplicationMsg exec() call");
        ApplicationContext.instance().get(Replicator.class).replicate();
    }
}
