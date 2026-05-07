package live.qsmc.discord2.plugins.events.message;

import live.qsmc.discord2.Bot;

public class MessageDeleteEvent extends MessageEvent<net.dv8tion.jda.api.events.message.MessageDeleteEvent> {


    public MessageDeleteEvent(Bot bot, net.dv8tion.jda.api.events.message.MessageDeleteEvent event) {
        super(bot, event);
    }
}