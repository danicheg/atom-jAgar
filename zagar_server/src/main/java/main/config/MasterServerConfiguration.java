package main.config;

import main.exceptions.WrongConfigBuildException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class MasterServerConfiguration {

    public static int ACCOUNT_PORT = 0;
    public static int CLIENT_PORT = 0;
    public static Class[] SERVICES_ARRAY = null;

    static {

        Properties props = new Properties();

        try (InputStream input = new FileInputStream(
                Paths.get("", "zagar_server", "target", "classes", "configuration.ini")
                        .toAbsolutePath().toFile())
        ) {

            props.load(input);
            ACCOUNT_PORT = Integer.valueOf(props.getProperty("accountServerPort"));
            CLIENT_PORT = Integer.valueOf(props.getProperty("clientConnectionPort"));

            final String[] services = props.getProperty("services").split(",");
            SERVICES_ARRAY = new Class[services.length];
            for (int i = 0; i < SERVICES_ARRAY.length; i++) {
                SERVICES_ARRAY[i] = Class.forName(props.getProperty(services[i]));
            }

        } catch (NumberFormatException e) {
            throw new WrongConfigBuildException("NumberFormatException " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new WrongConfigBuildException("ClassNotFoundException for class " + e);
        } catch (IOException e) {
            throw new WrongConfigBuildException("While trying opened configuration.ini being raised exception: " + e);
        }
    }

    private MasterServerConfiguration() {
        throw new IllegalAccessError(getClass() + " - utility class");
    }

}
