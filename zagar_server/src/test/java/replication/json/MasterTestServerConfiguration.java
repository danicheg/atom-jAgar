package replication.json;

import main.exceptions.WrongConfigBuildException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class MasterTestServerConfiguration {

    public static int ACCOUNT_PORT = 0;
    public static int CLIENT_PORT = 0;
    public static Class[] SERVICES_ARRAY = null;

    static {

        Properties props = new Properties();

        try (InputStream input = new FileInputStream(
                Paths.get("", "target", "test-classes", "test_replication_config.ini")
                        .toAbsolutePath()
                        .toFile()
        )) {

            props.load(input);

            try {
                ACCOUNT_PORT = Integer.valueOf(props.getProperty("accountServerPort"));
                CLIENT_PORT = Integer.valueOf(props.getProperty("clientConnectionPort"));
            } catch (NumberFormatException e) {
                throw new WrongConfigBuildException("NumberFormatException " + e.getMessage());
            }

            final String[] services = props.getProperty("services").split(",");
            SERVICES_ARRAY = new Class[services.length];
            for (int i = 0; i < SERVICES_ARRAY.length; i++) {
                try {
                    SERVICES_ARRAY[i] = Class.forName(props.getProperty(services[i]));
                } catch (ClassNotFoundException e) {
                    throw new WrongConfigBuildException("ClassNotFoundException for class " + e.getMessage());
                }
            }

        } catch (IOException e) {
            throw new WrongConfigBuildException("File test_replication_config.ini not found");
        }

    }

}
