package com.quiptmc.minecraft.api.events;

import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.events.listeners.Listener;

/**
 * Event triggered when a player joins the Quipt system.
 *
 * @param player the player who is joining
 * @param message the message associated with the player's arrival
 */
public record QuiptPlayerJoinEvent(MinecraftPlayer player, String message) implements QuiptEvent {

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