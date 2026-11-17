package live.qsmc.quipt.paper.assembler;

import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.assembler.model.AssemblyResult;
import live.qsmc.quipt.paper.commands.PaperCommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * Command handler for /quipt assembler.
 * Supports: generate, reload, and dry-run.
 */
public class AssemblerCommand extends PaperCommandExecutor {

    private static final Logger log = LoggerFactory.getLogger(AssemblerCommand.class);

    private final AssemblerService service;

    public AssemblerCommand(QuiptPlugin plugin, AssemblerService service) {
        super(plugin, "assembler");
        this.service = service;
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
                .then(literal("generate")
                        .then(argument("id", StringArgumentType.string())
                                .then(literal("--dry-run")
                                        .then(argument("seed", LongArgumentType.longArg())
                                                .executes(this::dryRunWithSeed))
                                        .executes(this::dryRunDefaultSeed))
                                .then(argument("seed", LongArgumentType.longArg())
                                        .executes(this::generateWithSeed))
                                .executes(this::generateDefaultSeed))
                )
                .then(literal("reload")
                        .executes(this::reload));
    }

    private int generateDefaultSeed(CommandContext<CommandSourceStack> context) {
        String id = StringArgumentType.getString(context, "id");
        CommandSender sender = context.getSource().getSender();

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can generate structures.");
            return 0;
        }

        Player player = (Player) sender;
        sender.sendMessage("Generating structure: " + id);

        CompletableFuture<AssemblyResult> future = service.generate(id, player.getLocation(), 0);
        future.whenComplete((result, error) -> {
            if (error != null) {
                sender.sendMessage("Generation failed: " + error.getMessage());
            } else {
                sender.sendMessage("Generated " + result.getPieceCount() + " pieces (cost: " + result.getTotalCost() + ")");
            }
        });

        return 1;
    }

    private int generateWithSeed(CommandContext<CommandSourceStack> context) {
        String id = StringArgumentType.getString(context, "id");
        long seed = LongArgumentType.getLong(context, "seed");
        CommandSender sender = context.getSource().getSender();

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can generate structures.");
            return 0;
        }

        Player player = (Player) sender;
        sender.sendMessage("Generating structure: " + id + " (seed: " + seed + ")");

        CompletableFuture<AssemblyResult> future = service.generate(id, player.getLocation(), seed);
        future.whenComplete((result, error) -> {
            if (error != null) {
                sender.sendMessage("Generation failed: " + error.getMessage());
            } else {
                sender.sendMessage("Generated " + result.getPieceCount() + " pieces (cost: " + result.getTotalCost() + ")");
            }
        });

        return 1;
    }

    private int dryRunDefaultSeed(CommandContext<CommandSourceStack> context) {
        return dryRun(context, 0);
    }

    private int dryRunWithSeed(CommandContext<CommandSourceStack> context) {
        long seed = LongArgumentType.getLong(context, "seed");
        return dryRun(context, seed);
    }

    private int dryRun(CommandContext<CommandSourceStack> context, long seed) {
        String id = StringArgumentType.getString(context, "id");
        CommandSender sender = context.getSource().getSender();

        sender.sendMessage("Dry run: " + id + (seed != 0 ? " (seed: " + seed + ")" : ""));

        CompletableFuture<AssemblyResult> future = service.dryRun(id, seed);
        future.whenComplete((result, error) -> {
            if (error != null) {
                sender.sendMessage("Dry run failed: " + error.getMessage());
            } else {
                sender.sendMessage("Dry run complete: " + result.getPieceCount() + " pieces (cost: " + result.getTotalCost() + ")");
                sender.sendMessage("Bounds: (" + result.getMinX() + "," + result.getMinY() + "," + result.getMinZ() + ") to (" +
                        result.getMaxX() + "," + result.getMaxY() + "," + result.getMaxZ() + ")");
            }
        });

        return 1;
    }

    private int reload(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        service.reload();
        sender.sendMessage("Assembler definitions reloaded.");
        return 1;
    }
}
