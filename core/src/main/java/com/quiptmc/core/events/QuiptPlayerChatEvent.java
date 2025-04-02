package com.quiptmc.core.events;

import com.quiptmc.core.entity.QuiptPlayer;
import com.quiptmc.core.events.listeners.Listener;

/**
 * Event triggered when a player sends a chat message in the Quipt system.
 *
 * @param player the player who sent the chat message
 * @param message the chat message sent by the player
 */
public record QuiptPlayerChatEvent(QuiptPlayer player, String message) implements QuiptEvent {

    /**
     * Returns the listener class associated with this event.
     *
     * @return the listener class associated with this event
     */
    @Override
    public Class<? extends Listener> listener() {
        return Listener.QuiptPlayerChatListener.class;
    }
}