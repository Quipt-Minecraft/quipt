package com.quiptmc2.discord.plugins.events.command;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.plugins.events.Event;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandInteractionEvent extends Event<SlashCommandInteractionEvent> {
    public CommandInteractionEvent(SlashCommandInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
