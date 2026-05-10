package live.qsmc.quipt.fabric.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.fabric.QuiptMod;
import live.qsmc.quipt.fabric.commands.CommandExecutor;
import live.qsmc.quipt.fabric.commands.SubCommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashMap;
import java.util.Map;

public class QuiptCommand extends CommandExecutor {

    private final ListCommand LIST_COMMAND;
    private final HelpCommand HELP_COMMAND;
    private final Map<String, QuiptSubCommandExecutor> SUB_COMMANDS = new HashMap<>();

    public QuiptCommand(QuiptMod mod) {
        super(mod, "quipt");
        LIST_COMMAND = register("list", ListCommand.class);
        HELP_COMMAND = register("help", HelpCommand.class);
    }

    private <T extends QuiptSubCommandExecutor> T register(String cmd, Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor(QuiptMod.class, QuiptCommand.class, String.class).newInstance(mod(), this, cmd);
            SUB_COMMANDS.put(cmd, instance);
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to register sub-command: " + cmd, e);
        }
    }

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return literal(name())
            .executes(context -> showUsage(context, permission(4)))
            .then(literal("list")
                .requires(context -> context.getPermissions().hasPermission(permission(4)))
                .executes(LIST_COMMAND))
            .then(literal("help")
                .executes(context -> showUsage(context, permission(4)))
                .then(argument("cmd", StringArgumentType.string())
                    .suggests((context,builder)-> onlySimilar(SUB_COMMANDS.keySet().toArray(new String[0]), "cmd", context, builder))
                    .executes(HELP_COMMAND)));

    }

    private static abstract class QuiptSubCommandExecutor extends SubCommandExecutor {
        public QuiptSubCommandExecutor(QuiptMod mod, QuiptCommand root, String cmd) {
            super(mod, root, cmd);
        }

        @Override
        public QuiptCommand root() {
            return (QuiptCommand) super.root();
        }

        public abstract Component help();
    }

    private static class HelpCommand extends QuiptSubCommandExecutor {

        public HelpCommand(QuiptMod mod, QuiptCommand root, String cmd) {
            super(mod, root, cmd);
        }

        @Override
        public Component help() {
            return Component.text("Usage: /quipt help <sub-command>")
                .appendNewline()
                .append(Component.text("Shows help for a sub-command", NamedTextColor.GRAY));
        }

        @Override
        public int run(CommandContext<ServerCommandSource> context) {
            String cmd = StringArgumentType.getString(context, "cmd");
            if(cmd == null || cmd.isEmpty())
                return logError(context, "No command specified");
            if(!root().SUB_COMMANDS.containsKey(cmd))
                return logError(context, "Unknown sub-command: " + cmd);
            QuiptSubCommandExecutor executor = root().SUB_COMMANDS.get(cmd);
            return log(context, executor.help(), NamedTextColor.GREEN, 1);
        }
    }

    private static class ListCommand extends QuiptSubCommandExecutor {

        public ListCommand(QuiptMod mod, QuiptCommand root, String cmd) {
            super(mod, root, cmd);
        }

        @Override
        public int run(CommandContext<ServerCommandSource> context) {
            Component text = Component.text("Quipt integrations:").appendNewline();
            boolean first = true;
            for (QuiptIntegration integration : Quipt.INSTANCE.integrations()) {
                text = text.append(Component.text(first ? integration.name() : ", " + integration.name()));
                first = false;
            }
            log(context, text.append(Component.text(".")), NamedTextColor.GREEN, 1);
            return 1;
        }

        @Override
        public Component help() {
            return Component.text("Usage: /quipt list").appendNewline().append(Component.text("Lists all available Quipt integrations",NamedTextColor.GRAY));
        }
    }
}
