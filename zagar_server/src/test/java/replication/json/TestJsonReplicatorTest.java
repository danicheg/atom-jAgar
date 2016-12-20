package replication.json;

import dao.Database;
import org.junit.Ignore;
import org.junit.Test;

public class TestJsonReplicatorTest {

    @Test
    @Ignore
    public void trySendJsonViaReplicator() throws InterruptedException {
        Database.openSession();
        MasterTestServer server = new MasterTestServer();
        server.start();
    }

}
