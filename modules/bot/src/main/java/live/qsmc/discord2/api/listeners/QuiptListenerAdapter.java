package live.qsmc.discord2.api.listeners;

import live.qsmc.discord2.Bot;
import live.qsmc.discord2.plugins.events.qda.interaction.command.CommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class QuiptListenerAdapter extends ListenerAdapter {

    private final Bot bot;

    public QuiptListenerAdapter(Bot bot){
        this.bot = bot;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        bot.plugins().events().handle(new live.qsmc.discord2.plugins.events.qda.message.MessageReceivedEvent(bot, event));
    }

    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        bot.plugins().events().handle(new live.qsmc.discord2.plugins.events.qda.message.MessageDeleteEvent(bot, event));
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        bot.plugins().events().handle(new live.qsmc.discord2.plugins.events.qda.interaction.button.ButtonInteractionEvent(event, bot));
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        bot.commands().handle(new CommandInteractionEvent(event, bot));
    }
}
