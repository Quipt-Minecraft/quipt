package live.qsmc.discord2.api.commands;

import live.qsmc.discord2.Bot;
import live.qsmc.discord2.plugins.events.BotEventHandler;

public class CommandManager extends BotEventHandler {
    public CommandManager(Bot bot) {
        super(bot, "commands");
    }
}
