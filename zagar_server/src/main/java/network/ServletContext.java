package network;

import org.eclipse.jetty.servlet.ServletContextHandler;

public class ServletContext {

    private static ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

    private ServletContext() {
        throw new IllegalAccessError(getClass() + "- utility class");
    }

    public static ServletContextHandler getInstance() {
        return context;
    }

}
