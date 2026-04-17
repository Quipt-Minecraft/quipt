package live.qsmc.minecraft.api.events.listeners;

import live.qsmc.minecraft.api.events.QuiptPlayerChatEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerDeathEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerJoinEvent;
import live.qsmc.minecraft.api.events.QuiptPlayerLeaveEvent;

/**
 * Represents a listener for Quipt events.
 */
public interface Listener {


    /**
     * Listener for player leave events.
     */
    interface QuiptPlayerLeaveListener extends Listener {
        /**
         * Called when a player leaves.
         *
         * @param event the player leaves event
         */
        void onPlayerLeave(QuiptPlayerLeaveEvent event);
    }

    /**
     * Listener for player death events.
     */
    interface QuiptPlayerDeathEventListener extends Listener {
        /**
         * Called when a player dies.
         *
         * @param event the player death event
         */
        void onPlayerDeath(QuiptPlayerDeathEvent event);
    }

    /**
     * Listener for player chat events.
     */
    interface QuiptPlayerChatListener extends Listener {
        /**
         * Called when a player sends a chat message.
         *
         * @param event the player chat event
         */
        void onPlayerChat(QuiptPlayerChatEvent event);
    }

    /**
     * Listener for player join events.
     */
    interface QuiptPlayerJoinListener extends Listener {
        /**
         * Called when a player joins.
         *
         * @param event the player join event
         */
        void onPlayerJoin(QuiptPlayerJoinEvent event);
    }
}