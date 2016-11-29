package replication;

import main.MasterTestServer;
import org.junit.Test;

public class JsonReplicatorTest {

    @Test
    public void trySendJsonViaReplicator() {

        /*try {
            MasterTestServer.main();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        JsonReplicator jsonReplicator = new JsonReplicator();
        jsonReplicator.replicate();
    }
}
