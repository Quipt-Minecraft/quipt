package com.quiptmc.core.server;

import com.quiptmc.core.QuiptIntegration;
import org.eclipse.jetty.server.Server;

/**
 * Represents the Quipt server, extending the Jetty Server.
 */
public class QuiptServer extends Server {

    /**
     * The QuiptIntegration instance associated with this server.
     */
    private final QuiptIntegration integration;

    /**
     * The QuiptHandler instance associated with this server.
     */
    private final QuiptHandler handler;

    /**
     * The configuration for the server.
     */
    private final ServerConfig config;

    /**
     * Constructs a new QuiptServer with the specified integration and configuration.
     *
     * @param integration the QuiptIntegration instance to associate with this server
     * @param config the configuration for the server
     */
    public QuiptServer(QuiptIntegration integration, ServerConfig config) {
        super(config.port());
        this.integration = integration;
        this.handler = new QuiptHandler(this);
        this.config = config;
        handle(handler);
    }

    /**
     * Returns the QuiptHandler instance associated with this server.
     *
     * @return the QuiptHandler instance associated with this server
     */
    public QuiptHandler handler(){
        return handler;
    }

    /**
     * Returns the QuiptIntegration instance associated with this server.
     *
     * @return the QuiptIntegration instance associated with this server
     */
    public QuiptIntegration integration(){
        return integration;
    }

    /**
     * Sets the handler for the server.
     *
     * @param handler the QuiptHandler instance to set
     */
    private void handle(QuiptHandler handler) {
        setHandler(handler);
    }

    /**
     * Returns the configuration for the server.
     *
     * @return the configuration for the server
     */
    public ServerConfig config() {
        return config;
    }

    /**
     * Represents the configuration for the Quipt server.
     *
     * @param protocol the protocol used by the server
     * @param host the host address of the server
     * @param port the port number of the server
     */
    public record ServerConfig(ServerProtocol protocol, String host, int port) {

        /**
         * Returns the address of the server.
         *
         * @return the address of the server
         */
        public String address(){
            return protocol().prefix() + host() + ":" + port();
        }

    }

    /**
     * Represents the protocol used by the Quipt server.
     */
    public enum ServerProtocol {
        HTTP("http://"), HTTPS("https://");

        /**
         * The prefix associated with the protocol.
         */
        final String prefix;

        /**
         * Constructs a new ServerProtocol with the specified prefix.
         *
         * @param prefix the prefix associated with the protocol
         */
        ServerProtocol(String prefix) {
            this.prefix = prefix;
        }

        /**
         * Returns the prefix associated with the protocol.
         *
         * @return the prefix associated with the protocol
         */
        public String prefix() {
            return prefix;
        }
    }
}