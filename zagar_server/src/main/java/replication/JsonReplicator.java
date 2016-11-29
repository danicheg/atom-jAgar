package replication;

import main.ApplicationContext;
import network.ClientConnections;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonReplicator implements Replicator {

    private static final Logger log = LogManager.getLogger(JsonReplicator.class);

    private static InputStream jsFileIO = JsonReplicator.class
            .getClassLoader()
            .getResourceAsStream("replic.json");
    private static String json;

    static {
        try {
            if (jsFileIO == null) {
                log.error("File replic.json not found");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(jsFileIO));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) out.append(line);
                json = out.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
        }
    }

    @Override
    public void replicate() {
        if (json == null) return;

        log.info("Sending test replic {}",json);

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
