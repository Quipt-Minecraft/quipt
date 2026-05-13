package live.qsmc.quipt.minecraft.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.discord.Webhook;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.commands.Command;
import live.qsmc.quipt.minecraft.commands.CommandBuilder;
import live.qsmc.quipt.minecraft.commands.CommonCommand;
import live.qsmc.quipt.minecraft.commands.executors.quipt.*;

import java.util.Iterator;import static net.kyori.adventure.text.Component.text;

public class WebhookCommand<S> extends CommonCommand<S> {

    public WebhookCommand(Command<S> command, MinecraftIntegration<?> integration) {
        super(command, integration);
    }

    @Override
    public LiteralArgumentBuilder<S> arguments(CommandBuilder<S> builder) {
        return builder.literal(command().name())
            .requires(sender -> command().hasPermission(sender, ""))
            .executes(context -> command().showUsage(context, "lastlife.admin"))
            .then(builder.literal("add")
                .executes(context -> command().showUsage(context, "lastlife.admin"))
                .then(builder.argument("id", StringArgumentType.word())
                    .then(builder.argument("token", StringArgumentType.word())
                        .then(builder.argument("channel", StringArgumentType.word())
                            .executes(context -> {
                                S sender = context.getSource();
                                if (!command().hasPermission(sender, ""))
                                    return command().logError(context, "You do not have permission to use this command.");

                                String id = StringArgumentType.getString(context, "id");
                                String token = StringArgumentType.getString(context, "token");
                                String channel = StringArgumentType.getString(context, "channel");

                                // Register with runtime manager first
                                Quipt.INSTANCE.webhooks().add(id, channel, token);
                                Webhook wh = Quipt.INSTANCE.webhooks().get(id);
                                if (wh == null)
                                    return command().logError(context, "Failed to create webhook. Please check your inputs.");

                                // Persist to config and save
                                Quipt.INSTANCE.webhooks().webhooks.put(wh);
                                Quipt.INSTANCE.webhooks().save();
                                command().sendMessage(sender, text("Added webhook '" + id + "' and saved to config."));
                                return 1;
                            })))))
            .then(builder.literal("remove")
                .executes(context -> command().showUsage(context, "lastlife.admin"))
                .then(builder.argument("id", StringArgumentType.word())
                    .executes(context -> {
                        S sender = context.getSource();
                        if (!command().hasPermission(sender, ""))
                            return command().logError(context, "You do not have permission to use this command.");

                        String id = StringArgumentType.getString(context, "id");

                        // Try treat provided id as channel name first
                        boolean removed = false;
                        Webhook existing = Quipt.INSTANCE.webhooks().webhooks.get(id);
                        if (existing != null) {
                            Quipt.INSTANCE.webhooks().webhooks.remove(id);
                            removed = true;
                        } else {
                            // Fallback: search by underlying discord id
                            Iterator<Webhook> it = Quipt.INSTANCE.webhooks().webhooks.values().iterator();
                            String keyToRemove = null;
                            while (it.hasNext()) {
                                Webhook w = it.next();
                                try {
                                    // Try common accessor names
                                    String discordId;
                                    try {
                                        discordId = (String) w.getClass().getMethod("id").invoke(w);
                                    } catch (NoSuchMethodException nsme) {
                                        discordId = (String) w.getClass().getMethod("webhookId").invoke(w);
                                    }
                                    if (id.equalsIgnoreCase(discordId)) {
                                        keyToRemove = w.name();
                                        break;
                                    }
                                } catch (Exception ignore) {
                                    // If reflection fails, skip
                                }
                            }
                            if (keyToRemove != null) {
                                Quipt.INSTANCE.webhooks().webhooks.remove(keyToRemove);
                                removed = true;
                            }
                        }

                        if (!removed) {
                            return command().logError(context, "Webhook not found by channel or id: " + id);
                        }

                        Quipt.INSTANCE.webhooks().save();
                        command().sendMessage(sender, text("Removed webhook '" + id + "' and saved to config."));
                        return 1;
                    })));
    }
}
