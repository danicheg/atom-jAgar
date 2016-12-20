package messagesystem.messages;

import main.ApplicationContext;
import mechanics.Mechanics;
import messagesystem.Abonent;
import messagesystem.Address;
import messagesystem.Message;
import messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import replication.LeaderBoardSender;
import replication.LeaderBoarder;

public class ReplicateLbd extends Message {

    @NotNull
    private static final Logger log = LogManager.getLogger(ReplicateLbd.class);

    public ReplicateLbd(Address from) {
        super(from, ApplicationContext.instance().get(MessageSystem.class).getService(
                Mechanics.class).getAddress()
        );
    }

    @Override
    public void exec(Abonent abonent) {
        log.info("ReplicationMsg exec() call");
        ApplicationContext.instance().get(LeaderBoarder.class).replicateLeaderBoard();
    }
}
