package com.quiptmc.core.logger;


import com.quiptmc.core.QuiptIntegration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Represents a logger
 */
public class QuiptLogger {

    QuiptIntegration integration;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Creates a new logger
     *
     * @param integration The integration that owns this logger
     */
    public QuiptLogger(QuiptIntegration integration) {
        this.integration = integration;
    }

    /**
     * Logs a message
     *
     * @param message The message to log
     */
    public void log(String message) {
        logger.info(message);
    }

    /**
     * Logs a message with arguments
     *
     * @param message The message to log
     * @param args    The arguments to format the message with
     */
    public void log(String message, Object... args) {
        logger.info(message, args);
    }

    /**
     * Logs a warning
     *
     * @param message The warning to log
     */
    public void warn(String message) {
        logger.warn(message);
    }

    /**
     * Logs a warning with arguments
     *
     * @param message The warning to log
     * @param args    The arguments to format the warning with
     */
    public void warn(String message, Object... args) {
        logger.warn(message, args);
    }

    /**
     * Logs an error
     *
     * @param message The error to log
     */
    public void error(String message) {
        logger.error(message);
    }

    /**
     * Logs an error with arguments
     *
     * @param message The error to log
     * @param args    The arguments to format the error with
     */
    public void error(String message, Object... args) {
        logger.error(message, args);
    }

    /**
     * Logs an error with a throwable
     *
     * @param message   The error to log
     * @param throwable The throwable to log
     */
    public void error(String message, Throwable throwable) {
        logger.error(message);
        throwable.printStackTrace();
    }

    /**
     * Logs an error with a throwable and arguments
     *
     * @param message   The error to log
     * @param throwable The throwable to log
     * @param args      The arguments to format the error with
     */
    public void error(String message, Throwable throwable, Object... args) {
        logger.error(message, args);
        throwable.printStackTrace();
    }

    /**
     * Logs a debug message
     *
     * @param message The debug message to log
     */
    public void debug(String message) {
        logger.debug(message);
    }

    /**
     * Logs a debug message with arguments
     *
     * @param message The debug message to log
     * @param args    The arguments to format the debug message with
     */
    public void debug(String message, Object... args) {
        logger.debug(message, args);
    }

    /**
     * Logs a trace message
     *
     * @param message The trace message to
     */
    public void trace(String message) {
        logger.trace(message);
    }

    /**
     * Logs a trace message with arguments
     *
     * @param message The trace message to log
     * @param args    The arguments to format the trace message with
     */
    public void trace(String message, Object... args) {
        logger.trace(message, args);
    }


}
