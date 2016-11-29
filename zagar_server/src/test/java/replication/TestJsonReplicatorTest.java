package replication;

import org.junit.Test;

public class TestJsonReplicatorTest {

    @Test
    public void trySendJsonViaReplicator() {

        /*try {
            MasterTestServer.main();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        TestJsonReplicator jsonReplicator = new TestJsonReplicator();
        jsonReplicator.replicate();
    }
}
