package replication.leaderboard;

import dao.Database;
import org.junit.Ignore;
import org.junit.Test;

public class TestLeaderBoardTestReplicator {

    @Test
    @Ignore
    public void trySendLeaderBoardJsonViaReplicator() throws InterruptedException {
        Database.openSession();
        MasterTestLeaderServer server = new MasterTestLeaderServer();
        server.start();
    }
}
