package com.quiptmc.minecraft.api.events;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.minecraft.api.events.listeners.Listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the registration and dispatching of Quipt events to their respective listeners.
 */
public class EventHandler {

    private final QuiptIntegration integration;
    private final List<Listener> listeners = new ArrayList<>();

    /**
     * Constructs an EventHandler with the specified QuiptIntegration.
     *
     * @param integration the QuiptIntegration instance
     */
    public EventHandler(QuiptIntegration integration) {
        this.integration = integration;
    }

    /**
     * Registers a listener to receive events.
     *
     * @param listener the listener to register
     */
    public void register(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Handles the specified event by dispatching it to the appropriate listener.
     *
     * @param event the event to handle
     */
    public void handle(QuiptEvent event) {
        if (event instanceof QuiptPlayerJoinEvent joinEvent) {
            for (Listener listener : listeners) {
                if (listener instanceof Listener.QuiptPlayerJoinListener) {
                    ((Listener.QuiptPlayerJoinListener) listener).onPlayerJoin(joinEvent);
                }
            }
        }
        if (event instanceof QuiptPlayerLeaveEvent leaveEvent) {
            for (Listener listener : listeners) {
                if (listener instanceof Listener.QuiptPlayerLeaveListener) {
                    ((Listener.QuiptPlayerLeaveListener) listener).onPlayerLeave(leaveEvent);
                }
            }
        }
        if (event instanceof QuiptPlayerDeathEvent deathEvent) {
            for (Listener listener : listeners) {
                if (listener instanceof Listener.QuiptPlayerDeathEventListener) {
                    ((Listener.QuiptPlayerDeathEventListener) listener).onPlayerDeath(deathEvent);
                }
            }
        }
        if (event instanceof QuiptPlayerChatEvent chatEvent) {
            for (Listener listener : listeners) {
                if (listener instanceof Listener.QuiptPlayerChatListener) {
                    ((Listener.QuiptPlayerChatListener) listener).onPlayerChat(chatEvent);
                }
            }
        }

    }
    /**
     * Dispatches the specified event to the appropriate listener.
     *
     * @param event the event to dispatch
     * @param listener the listener to dispatch the event to
     */
    private void handleEvent(QuiptEvent event, Listener listener) {
        if (listener instanceof Listener.QuiptPlayerJoinListener joinListener) {
            joinListener.onPlayerJoin((QuiptPlayerJoinEvent) event);
        } else if (listener instanceof Listener.QuiptPlayerLeaveListener leaveListener) {
            leaveListener.onPlayerLeave((QuiptPlayerLeaveEvent) event);
        } else if (listener instanceof Listener.QuiptPlayerDeathEventListener deathListener) {
            deathListener.onPlayerDeath((QuiptPlayerDeathEvent) event);
        } else if (listener instanceof Listener.QuiptPlayerChatListener chatListener) {
            chatListener.onPlayerChat((QuiptPlayerChatEvent) event);
        }
    }
}