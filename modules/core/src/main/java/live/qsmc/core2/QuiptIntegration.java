package live.qsmc.core2;


import live.qsmc.core2.config.ConfigManager;
import live.qsmc.core2.config.factories.GenericFactory;
import live.qsmc.core2.config.files.QuiptConfig;
import live.qsmc.core2.data.registries.Registries;
import live.qsmc.core2.heartbeat.Heartbeat;
import live.qsmc.core2.server.QuiptServer;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;

public abstract class QuiptIntegration {

    /**
     * Logger instance for this integration
     */
    private final Logger logger = new Logger();
    /**
     * Heartbeat instance for this integration
     */
    private final Heartbeat heartbeat = new Heartbeat(this);
    /**
     * Config manager instance for this integration
     */
    private ConfigManager configs = null;


    private QuiptServer server = null;

    public QuiptIntegration() {
        logger().log("Quipt", "Initializing QuiptIntegration {}...", getClass().getName());
    }

    public final Logger logger() {
        return logger;
    }

    public QuiptServer server() {
        if(server == null){
            logger().log("Server", "Initializing QuiptServer...");
            if(configs().config(QuiptConfig.class) == null){
                logger().log("Server", "Initializing Webhook Config...");
                configs.factory(new GenericFactory<>(QuiptConfig.WebData.class));
                configs.register(QuiptConfig.class);
            }
            QuiptConfig.WebData webData = configs.config(QuiptConfig.class).webData;
            QuiptServer.ServerConfig serverConfig = new QuiptServer.ServerConfig(QuiptServer.ServerProtocol.valueOf(webData.protocol.toUpperCase(Locale.ROOT)), webData.host, webData.port);
            server = new QuiptServer(this, serverConfig);
        }
        return server;
    }

    public Heartbeat heartbeat() {
        return heartbeat;
    }


    public ConfigManager configs() {
        if(configs == null){
            logger().log("ConfigManager", "Initializing Config Manager...");
            configs = new ConfigManager(this);
        }
        return configs;
    }



    public abstract String name();
    public abstract String version();
    public abstract File folder();
    public abstract void enable();


    public class Logger {

        org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

        Logger() {}

        /**
         * Logs a message with a specified tag.
         *
         * @param tag     The tag identifying the source or type of the log message.
         * @param message The message to be logged.
         */
        public void log(String tag, String message) {
            logger.info("[{}-{}] {}", name(), tag, message);
        }

        /**
         * Logs a message with arguments
         *
         * @param tag     The tag identifying the source or type of the log message.
         * @param message The message to log
         * @param args    The arguments to format the message with
         */
        public void log(String tag, String message, Object... args) {
            String formatted = "[" + name() + "-" + tag + "] " + message;
            logger.info(formatted, args);
        }

        /**
         * Logs a warning
         *
         * @param tag     The tag identifying the source or type of the warning message.
         * @param message The warning to log
         */
        public void warn(String tag, String message) {
            logger.warn("[{}-{}] {}", name(), tag, message);
        }

        /**
         * Logs a warning with arguments
         *
         * @param tag     The tag identifying the source or type of the warning message.
         * @param message The warning to log
         * @param args    The arguments to format the warning with
         */
        public void warn(String tag, String message, Object... args) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.warn(formatted, args);
        }

        /**
         * Logs an error
         *
         * @param tag     The tag identifying the source or type of the error message.
         * @param message The error to log
         */
        public void error(String tag, String message) {
            logger.error("[{}-{}] {}", name(), tag, message);
        }

        /**
         * Logs an error with arguments
         *
         * @param tag     The tag identifying the source or type of the error message.
         * @param message The error to log
         * @param args    The arguments to format the error with
         */
        public void error(String tag, String message, Object... args) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.error(formatted, args);
        }

        /**
         * Logs an error with a throwable
         *
         * @param tag         The tag identifying the source or type of the error message.
         * @param message     The error to log
         * @param throwable   The throwable to log
         */
        public void error(String tag, String message, Throwable throwable) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.error(formatted, throwable);
        }

        /**
         * Logs an error with a throwable and arguments
         *
         * @param tag         The tag identifying the source or type of the error message.
         * @param message     The error to log
         * @param throwable   The throwable to log
         * @param args        The arguments to format the error with
         */
        public void error(String tag, String message, Throwable throwable, Object... args) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.error(formatted, args, throwable);
        }

        /**
         * Logs a debug message
         *
         * @param tag     The tag identifying the source or type of the debug message.
         * @param message The debug message to log
         */
        public void debug(String tag, String message) {
            logger.debug("[{}-{}] {}", name(), tag, message);
        }

        /**
         * Logs a debug message with arguments
         *
         * @param tag     The tag identifying the source or type of the debug message.
         * @param message The debug message to log
         * @param args    The arguments to format the debug message with
         */
        public void debug(String tag, String message, Object... args) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.debug(formatted, args);
        }

        /**
         * Logs a trace message
         *
         * @param tag     The tag identifying the source or type of the trace message.
         * @param message The trace message to
         */
        public void trace(String tag, String message) {
            logger.trace("[{}-{}] {}", name(), tag, message);
        }

        /**
         * Logs a trace message with arguments
         *
         * @param tag     The tag identifying the source or type of the trace message.
         * @param message The trace message to log
         * @param args    The arguments to format the trace message with
         */
        public void trace(String tag, String message, Object... args) {
            String formatted = "[" + "" + tag + "] " + message;
            logger.trace(formatted, args);
        }


    }

}
