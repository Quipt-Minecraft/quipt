package com.quiptmc.discord2.plugins.events.qda.interaction.command;

import com.quiptmc.discord2.Bot;
import com.quiptmc.discord2.plugins.events.qda.DiscordEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandInteractionEvent extends DiscordEvent<SlashCommandInteractionEvent> {
    public CommandInteractionEvent(SlashCommandInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
