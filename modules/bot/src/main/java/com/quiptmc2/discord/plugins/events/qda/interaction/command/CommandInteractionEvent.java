package com.quiptmc2.discord.plugins.events.qda.interaction.command;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.plugins.events.qda.DiscordEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandInteractionEvent extends DiscordEvent<SlashCommandInteractionEvent> {
    public CommandInteractionEvent(SlashCommandInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
