package live.qsmc.quipt.paper.commands.executors;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.quipt.minecraft.events.party.Party;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import live.qsmc.quipt.paper.QuiptPaper;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.api.players.PaperPlayer;
import live.qsmc.quipt.paper.commands.PaperCommandExecutor;
import live.qsmc.quipt.paper.commands.executors.arguments.PartyArgumentType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

import java.util.List;


public class PartyCommand extends PaperCommandExecutor {

    public PartyCommand(QuiptPlugin plugin) {
        super(plugin, "party");
    }

    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
            .requires(source -> source.getSender().hasPermission("quipt.party"))
            .executes(context -> showUsage(context, permission("quipt.party")))
            .then(literal("create")
                .requires(source -> source.getSender().hasPermission("quipt.party.create"))
                .executes(context -> showUsage(context, permission("quipt.party.create")))
                .then(argument("partyName", StringArgumentType.word())
                    .executes(context -> {
                        if (!context.getSource().getSender().hasPermission("quipt.party.create"))
                            return logError(context, "You do not have permission to use this command.");
                        String partyName = StringArgumentType.getString(context, "partyName");
                        
                        if (plugin().integration().parties().get(partyName) != null)
                            return logError(context, "A party with that name already exists.");
                        plugin().integration().parties().create(partyName);
                        context.getSource().getSender().sendMessage(MessageUtils.get("quipt.party.create", partyName));
                        return Command.SINGLE_SUCCESS;
                    })))
            .then(literal("leave")
                .requires(source -> source.getSender().hasPermission("quipt.party.leave"))
                .executes(context -> {
                    if (!context.getSource().getSender().hasPermission("quipt.party.leave"))
                        return logError(context, "You do not have permission to use this command.");
                    if (!(context.getSource().getSender() instanceof Player target))
                        return logError(context, "Only players can leave parties.");
                    PaperPlayer paperPlayer = QuiptPaper.instance().players().of(target);
                    Party party = plugin().integration().parties().get(paperPlayer).orElse(null);
                    if (party == null)
                        return logError(context, "You are not in a party.");
                    party.leave(paperPlayer);





                    return Command.SINGLE_SUCCESS;

                })
                .then(argument("target", ArgumentTypes.players())
                    .requires(source -> source.getSender().hasPermission("quipt.party.leave.other"))
                    .executes(context -> {
                        if (!context.getSource().getSender().hasPermission("quipt.party.leave.other"))
                            return logError(context, "You do not have permission to use this command.");
                        PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                        List<Player> targets = targetResolver.resolve(context.getSource());
                        for (Player target : targets) {
                            PaperPlayer paperTarget = QuiptPaper.instance().players().of(target);

                            Party party = plugin().integration().parties().get(paperTarget).orElse(null);
                            if (party == null) {
                                logError(context, target.getName() + " is not in a party.");
                                continue;
                            }
                            context.getSource().getSender().sendMessage(MessageUtils.get("quipt.party.leave.other", target.getName(), party.id()));
                            party.leave(paperTarget);
                        }
                        return Command.SINGLE_SUCCESS;
                    })))
            .then(literal("join")
                .requires(source -> source.getSender().hasPermission("quipt.party.join"))
                .executes(context -> showUsage(context, permission("quipt.party.join")))
                .then(argument("partyName", new PartyArgumentType(plugin(), "partyName"))
                    .executes(context -> {
                        if (!context.getSource().getSender().hasPermission("quipt.party.join"))
                            return logError(context, "You do not have permission to use this command.");
                        if (!(context.getSource().getSender() instanceof Player target))
                            return logError(context, "Only players can join parties.");
                        try {
                            Party party = context.getArgument("partyName", Party.class);
                            party.join(QuiptPaper.instance().players().of(target));
                        } catch (NullPointerException e) {
                            return logError(context, "Party not found.");
                        }
                        return 0;
                    })
                    .then(argument("target", ArgumentTypes.players())
                        .executes(context -> {
                            if (!context.getSource().getSender().hasPermission("quipt.party.join"))
                                return logError(context, "You do not have permission to use this command.");
                            Party party = context.getArgument("partyName", Party.class);
                            if (party == null)
                                return logError(context, "Party not found.");

                            PlayerSelectorArgumentResolver targetResolver = context.getArgument("target", PlayerSelectorArgumentResolver.class);
                            List<Player> targets = targetResolver.resolve(context.getSource());
                            for (Player target : targets) {
                                context.getSource().getSender().sendMessage(MessageUtils.get("quipt.party.join.other", target.getName(), party.id()));
                                party.join(QuiptPaper.instance().players().of(target));
                            }
                            return Command.SINGLE_SUCCESS;
                        }))))
            .then(literal("remove")
                .requires(source -> source.getSender().hasPermission("quipt.party.remove"))
                .executes(context -> showUsage(context, permission("quipt.party.remove")))
                .then(argument("partyName", new PartyArgumentType(plugin(), "partyName"))
                    .executes(context -> {
                        if (!context.getSource().getSender().hasPermission("quipt.party.remove"))
                            return logError(context, "You do not have permission to use this command.");
                        try {
                            Party party = context.getArgument("partyName", Party.class);
                            plugin().integration().parties().remove(party);
                            context.getSource().getSender().sendMessage(MessageUtils.get("quipt.party.remove", party.id()));
                            return Command.SINGLE_SUCCESS;
                        } catch (NullPointerException e) {
                            return logError(context, "Party not found.");
                        }
                    })));
    }
}
