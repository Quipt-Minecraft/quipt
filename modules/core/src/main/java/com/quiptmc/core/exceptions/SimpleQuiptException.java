/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.exceptions;


/**
 * A simple exception for the Quipt API
 */
public class SimpleQuiptException extends RuntimeException {

    /**
     * Create a new exception with a message
     *
     * @param message The message to display
     */
    public SimpleQuiptException(String message) {
        super(message);

    }
}
