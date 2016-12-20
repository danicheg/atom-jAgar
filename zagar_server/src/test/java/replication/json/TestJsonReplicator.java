package replication.json;

import main.ApplicationContext;
import network.ClientConnections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import replication.Replicator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestJsonReplicator implements Replicator {

    private static final Logger log = LogManager.getLogger(TestJsonReplicator.class);

    private static String json;

    static {
        try {
            json = new String(Files.readAllBytes(
                    Paths.get("", "target", "test-classes", "replic.json")))
                    .replace("\n", "").replace("\r", "");
        } catch (IOException e) {
            json = null;
            log.error("Can not find or open replic.json");
        }
    }

    @Override
    public void replicateState() {
        if (json == null) return;

        log.info("Sending test replic {}", json);

        try {
            ApplicationContext.instance().get(ClientConnections.class)
                    .getConnections()
                    .forEach((connection) -> {
                        try {
                            if (connection.getValue().isOpen())
                                connection.getValue().getRemote().sendString(json);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
