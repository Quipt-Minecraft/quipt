package live.qsmc.fabric2.commands.executors;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.config.files.QuiptConfig;
import live.qsmc.core2.utils.net.ApiResponse;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.HttpHeaders;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.fabric2.QuiptMod;
import live.qsmc.fabric2.commands.CommandExecutor;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Identifier;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.net.http.HttpResponse;

import static com.mojang.brigadier.builder.RequiredArgumentBuilder.argument;


public class AccountCommand extends CommandExecutor {
    public AccountCommand(QuiptMod mod) {
        super(mod, "account");
    }

    private final Permission accountPerm = new Permission.Atom(Identifier.of("quipt.account"));
    private final Permission helpPerm = new Permission.Atom(Identifier.of("quipt.account.help"));
    private final Permission linkPerm = new Permission.Atom(Identifier.of("quipt.account.link"));
    private final Permission registerPerm = new Permission.Atom(Identifier.of("quipt.account.register"));

    @Override
    public LiteralArgumentBuilder<ServerCommandSource> arguments() {
        return literal(name())
            .requires(sender -> sender.getPermissions().hasPermission(accountPerm))
            .executes(context -> showUsage(context, accountPerm))
            .then(literal("help")
                .requires(sender -> sender.getPermissions().hasPermission(helpPerm))
                .executes(context -> showUsage(context, helpPerm)))
            .then(literal("token")
                .requires(sender -> sender.getPermissions().hasPermission(linkPerm))
                .executes(context -> showUsage(context, linkPerm))

                .then(argument("access_token", StringArgumentType.string())
                    .executes(context -> {

                        try {
                            String access_token = StringArgumentType.getString(context, "access_token");
                            HttpConfig httpConfig = HttpConfig.defaults(HttpHeaders.AUTHORIZATION_BEARER(access_token));
                            String apiUrl = "https://api.qsmc.live/token/validate";
                            HttpResponse<String> responseRaw = NetworkUtils.get(httpConfig, apiUrl);
                            ApiResponse<?> response = new ApiResponse<>(responseRaw);
                            if (response.isFailure()) {
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
                .requires(context -> context.getPermissions().hasPermission(registerPerm))
                .executes(context -> showUsage(context, registerPerm))
                .then(argument("username", StringArgumentType.string())
                    .executes(context -> showUsage(context, registerPerm))
                    .then(argument("password", StringArgumentType.string())
                        .executes(context -> showUsage(context, registerPerm))
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
