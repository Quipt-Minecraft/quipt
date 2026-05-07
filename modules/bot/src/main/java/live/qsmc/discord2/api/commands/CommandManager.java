package live.qsmc.discord2.api.commands;

import live.qsmc.core2.events.EventHandler;
import live.qsmc.discord2.Bot;

public class CommandManager extends EventHandler {
    public CommandManager(Bot bot) {
        super(bot, "commands");
    }
}
