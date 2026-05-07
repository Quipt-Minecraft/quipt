package live.qsmc.discord2.plugins.events.interaction.button;

import live.qsmc.discord2.Bot;
import live.qsmc.discord2.plugins.events.DiscordEvent;

public class ButtonInteractionEvent extends DiscordEvent<net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent> {

    public ButtonInteractionEvent(net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
