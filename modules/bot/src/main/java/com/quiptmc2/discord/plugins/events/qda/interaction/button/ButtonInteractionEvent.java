package com.quiptmc2.discord.plugins.events.qda.interaction.button;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.plugins.events.qda.DiscordEvent;

public class ButtonInteractionEvent extends DiscordEvent<net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent> {

    public ButtonInteractionEvent(net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
