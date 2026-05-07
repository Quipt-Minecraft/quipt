package live.qsmc.quipt.paper.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.config.files.QuiptConfig;
import live.qsmc.quipt.core.utils.net.ApiResponse;
import live.qsmc.quipt.core.utils.net.HttpConfig;
import live.qsmc.quipt.core.utils.net.HttpHeaders;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import live.qsmc.quipt.paper.QuiptPlugin;
import live.qsmc.quipt.paper.commands.CommandExecutor;
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

                        try {
                            String access_token = StringArgumentType.getString(context, "access_token");
                            HttpConfig httpConfig = HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER(access_token));
                            String apiUrl = "https://api.qsmc.live/token/validate";
                            HttpResponse<String> responseRaw = NetworkUtils.get(httpConfig, apiUrl);
                            ApiResponse<?> response = new ApiResponse<>(responseRaw);
                            if(response.isFailure()){
                                return logError(context, "Invalid access token");
                            }
                            QuiptConfig config = Quipt.INSTANCE.configs().config(QuiptConfig.class);
                            config.access_token = access_token;
                            config.save();
                            return logSuccess(context, "Access token is valid and has been saved.");
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
