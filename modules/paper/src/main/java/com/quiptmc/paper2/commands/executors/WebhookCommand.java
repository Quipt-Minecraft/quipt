package com.quiptmc.paper2.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.quiptmc.core2.Quipt;
import com.quiptmc.core2.discord.Webhook;
import com.quiptmc.paper2.QuiptPlugin;
import com.quiptmc.paper2.commands.CommandExecutor;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;

import java.util.Iterator;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class WebhookCommand extends CommandExecutor {

    public WebhookCommand(QuiptPlugin plugin) {
        super(plugin, "webhook");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
                .executes(context -> showUsage(context, "lastlife.admin"))
                .then(literal("add")
                        .executes(context -> showUsage(context, "lastlife.admin"))
                        .then(argument("id", StringArgumentType.word())
                                .then(argument("token", StringArgumentType.word())
                                        .then(argument("channel", StringArgumentType.word())
                                                .executes(context -> {
                                                    CommandSender sender = context.getSource().getSender();
                                                    if (!sender.hasPermission("lastlife.admin"))
                                                        return logError(context, "You do not have permission to use this command.");

                                                    String id = StringArgumentType.getString(context, "id");
                                                    String token = StringArgumentType.getString(context, "token");
                                                    String channel = StringArgumentType.getString(context, "channel");

                                                    // Register with runtime manager first
                                                    Quipt.INSTANCE.webhooks().add(id, channel, token);
                                                    Webhook wh = Quipt.INSTANCE.webhooks().get(id);
                                                    if (wh == null)
                                                        return logError(context, "Failed to create webhook. Please check your inputs.");

                                                    // Persist to config and save
                                                    Quipt.INSTANCE.webhooks().webhooks.put(wh);
                                                    Quipt.INSTANCE.webhooks().save();

                                                    sender.sendMessage(net.kyori.adventure.text.Component.text("Added webhook '" + id + "' and saved to config."));
                                                    return 1;
                                                })))))
                .then(literal("remove")
                        .executes(context -> showUsage(context, "lastlife.admin"))
                        .then(argument("id", StringArgumentType.word())
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    if (!sender.hasPermission("lastlife.admin"))
                                        return logError(context, "You do not have permission to use this command.");

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
                                        return logError(context, "Webhook not found by channel or id: " + id);
                                    }

                                    Quipt.INSTANCE.webhooks().save();
                                    sender.sendMessage(net.kyori.adventure.text.Component.text("Removed webhook '" + id + "' from config."));
                                    return 1;
                                })));
    }
}
