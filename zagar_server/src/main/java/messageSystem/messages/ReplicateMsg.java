package messageSystem.messages;

import main.ApplicationContext;
import messageSystem.Abonent;
import messageSystem.Address;
import messageSystem.Message;
import messageSystem.MessageSystem;
import network.ClientConnectionServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import replication.Replicator;

public class ReplicateMsg extends Message {

    @NotNull
    private static final Logger log = LogManager.getLogger(ReplicateMsg.class);

    public ReplicateMsg(Address from) {
        super(from, ApplicationContext.instance().get(MessageSystem.class).getService(
                ClientConnectionServer.class).getAddress()
        );
    }

    @Override
    public void exec(Abonent abonent) {
        log.info("ReplicationMsg exec() call");
        ApplicationContext.instance().get(Replicator.class).replicate();
    }
}
