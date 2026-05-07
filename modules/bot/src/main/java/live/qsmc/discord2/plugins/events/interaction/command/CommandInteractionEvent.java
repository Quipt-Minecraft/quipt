package live.qsmc.discord2.plugins.events.interaction.command;

import live.qsmc.discord2.Bot;
import live.qsmc.discord2.plugins.events.DiscordEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class CommandInteractionEvent extends DiscordEvent<SlashCommandInteractionEvent> {
    public CommandInteractionEvent(SlashCommandInteractionEvent original, Bot bot) {
        super(original, bot);
    }
}
