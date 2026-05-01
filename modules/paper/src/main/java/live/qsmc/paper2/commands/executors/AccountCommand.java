package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.http.HttpResponse;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static net.kyori.adventure.text.Component.text;

public class AccountCommand extends CommandExecutor {
    public AccountCommand(QuiptPlugin plugin) {
        super(plugin, "account");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return literal(name())
            .requires(context -> context.getSender().hasPermission("quipt.account"))
            .executes(context -> showUsage(context, "quipt.account"))
            .then(literal("help")
                .requires(context -> context.getSender().hasPermission("quipt.account.help"))
                .executes(context -> showUsage(context, "quipt.account.help")))
            .then(literal("token")
                .requires(context -> context.getSender().hasPermission("quipt.account.link"))
                .executes(context -> showUsage(context, "quipt.account.link"))
                .then(argument("access_token", StringArgumentType.string())
                    .executes(context -> {

                        try {JSONObject request = new JSONObject();
                            HttpResponse<String> response = response = NetworkUtils.post(HttpConfig.defaults(), "https://api.qsmc.live/account/register", request);
                            JSONObject jsonResponse = new JSONObject(response.body());
                            return 1;
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    })))
            .then(literal("register")
                .requires(context -> context.getSender().hasPermission("quipt.account.link"))
                .executes(context -> showUsage(context, "quipt.account.link"))
                .then(argument("username", StringArgumentType.string())
                    .executes(context -> showUsage(context, "quipt.account.link"))
                    .then(argument("password", StringArgumentType.string())
                        .executes(context -> showUsage(context, "quipt.account.link"))
                        .then(argument("email", StringArgumentType.greedyString())
                            .executes(context -> {
                                JSONObject request = new JSONObject();
                                request.put("username", StringArgumentType.getString(context, "username"));
                                request.put("password", StringArgumentType.getString(context, "password"));
                                request.put("email", StringArgumentType.getString(context, "email"));
                                try {
                                    HttpResponse<String> response = NetworkUtils.post(HttpConfig.defaults(), "https://api.qsmc.live/account/register", request);
                                    JSONObject jsonResponse = new JSONObject(response.body());

                                    if (jsonResponse.has("error")) {
                                        return logError(context, "Registration failed: " + jsonResponse.getString("error"));
                                    }

                                    return logSuccess(context, MessageUtils.get("quipt.account.register.success"));
                                } catch (Exception e) {
                                    return logError(context, "An error occurred while registering your account: " + e.getMessage());
                                }
                            })))));

    }
}
