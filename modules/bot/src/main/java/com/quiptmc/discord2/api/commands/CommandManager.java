package com.quiptmc.discord2.api.commands;

import com.quiptmc.discord2.Bot;
import com.quiptmc.discord2.plugins.events.BotEventHandler;

public class CommandManager extends BotEventHandler {
    public CommandManager(Bot bot) {
        super(bot, "commands");
    }
}
