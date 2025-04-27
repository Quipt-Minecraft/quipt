package com.quiptmc.minecraft.api.events;

import com.quiptmc.minecraft.api.events.listeners.Listener;

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