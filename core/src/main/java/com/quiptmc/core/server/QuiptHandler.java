package com.quiptmc.core.server;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Handles the server context and servlet management for the Quipt system.
 */
public class QuiptHandler extends ServletContextHandler {

    /**
     * The QuiptServer instance associated with this handler.
     */
    QuiptServer server;

    /**
     * Constructs a new QuiptHandler with the specified server.
     *
     * @param server the QuiptServer instance to associate with this handler
     */
    public QuiptHandler(QuiptServer server) {
        super(SESSIONS);
        this.server = server;
        server.setHandler(this);
    }

    /**
     * Returns the QuiptServer instance associated with this handler.
     *
     * @return the QuiptServer instance associated with this handler
     */
    public QuiptServer server() {
        return server;
    }

    /**
     * Handles the addition of a servlet to the server.
     *
     * @param name the name of the servlet
     * @param servlet the QuiptServlet instance to add
     * @param path the path at which the servlet should be accessible
     */
    public void handle(String name, QuiptServlet servlet, String path) {
        path = path.startsWith("/") ? path.substring(1) : path;
        String url = server.config().address();
        addServlet(new ServletHolder(name, servlet), "/" + path);
        server.integration().logger().log("Added servlet %s at: %s/%s", name, url, path);
    }
}