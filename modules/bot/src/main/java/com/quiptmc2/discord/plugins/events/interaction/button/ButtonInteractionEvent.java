package com.quiptmc2.discord.plugins.events.interaction.button;

import com.quiptmc2.discord.Bot;
import com.quiptmc2.discord.api.Wrapper;
import com.quiptmc2.discord.plugins.events.Event;

public class ButtonInteractionEvent extends Event<net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent> {

    public ButtonInteractionEvent(net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
