package live.qsmc.discord2.plugins.events.qda.message;

import live.qsmc.discord2.Bot;
import live.qsmc.discord2.api.message.QuiptMessage;
import live.qsmc.discord2.api.user.QuiptUser;

public class MessageReceivedEvent extends MessageEvent<net.dv8tion.jda.api.events.message.MessageReceivedEvent> {

    public MessageReceivedEvent(Bot bot, net.dv8tion.jda.api.events.message.MessageReceivedEvent event) {
        super(bot, event);
    }

    public QuiptMessage message() {
        return new QuiptMessage(data().getMessage());
    }

    public QuiptUser author() {
        return guild().user(data().getAuthor());
    }
}
