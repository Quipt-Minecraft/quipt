package live.qsmc.discord2.plugins.events;

import live.qsmc.core2.events.Event;
import live.qsmc.discord2.Bot;

public abstract class DiscordEvent<T extends net.dv8tion.jda.api.events.Event> extends Event<T> {

    Bot bot;

    public DiscordEvent(T original, Bot bot) {
        super(original);

        this.bot = bot;
    }


    public Bot bot(){
        return bot;
    }
}
