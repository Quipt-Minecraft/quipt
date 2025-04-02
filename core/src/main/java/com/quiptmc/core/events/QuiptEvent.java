package com.quiptmc.core.events;

import com.quiptmc.core.events.listeners.Listener;

/**
 * Represents a Quipt event.
 */
public interface QuiptEvent {

    /**
     * Returns the listener class associated with this event.
     *
     * @return the listener class associated with this event
     */
    Class<? extends Listener> listener();

}