package com.quiptmc.core.events;

import com.quiptmc.core.entity.QuiptPlayer;
import com.quiptmc.core.events.listeners.Listener;

/**
 * Event triggered when a player joins the Quipt system.
 *
 * @param player the player who is joining
 * @param message the message associated with the player's arrival
 */
public record QuiptPlayerJoinEvent(QuiptPlayer player, String message) implements QuiptEvent {

    /**
     * Returns the listener class associated with this event.
     *
     * @return the listener class associated with this event
     */
    @Override
    public Class<? extends Listener> listener() {
        return Listener.QuiptPlayerJoinListener.class;
    }
}