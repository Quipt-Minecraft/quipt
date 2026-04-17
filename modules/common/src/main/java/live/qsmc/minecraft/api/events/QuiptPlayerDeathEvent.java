package live.qsmc.minecraft.api.events;

import live.qsmc.minecraft.api.MinecraftPlayer;
import live.qsmc.minecraft.api.events.listeners.Listener;
import org.eclipse.jgit.annotations.Nullable;

/**
 * Event triggered when a player dies in the Quipt system.
 *
 * @param player  the player who died
 * @param killer  the player who killed the player, or null if there was no killer
 * @param message the message associated with the player's death
 */
public record QuiptPlayerDeathEvent(MinecraftPlayer player, @Nullable MinecraftPlayer killer,
                                    String message) implements QuiptEvent {

    /**
     * Returns the listener class associated with this event.
     *
     * @return the listener class associated with this event
     */
    @Override
    public Class<? extends Listener> listener() {
        return Listener.QuiptPlayerDeathEventListener.class;
    }
}