package live.qsmc.paper2.commands.executors;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import live.qsmc.core2.Quipt;
import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.utils.net.HttpConfig;
import live.qsmc.core2.utils.net.HttpHeader;
import live.qsmc.core2.utils.net.HttpHeaders;
import live.qsmc.core2.utils.net.NetworkUtils;
import live.qsmc.paper2.QuiptPlugin;
import live.qsmc.paper2.commands.CommandExecutor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;
import static net.kyori.adventure.text.Component.text;

public class DumpCommand extends CommandExecutor {
    public DumpCommand(QuiptPlugin plugin) {
        super(plugin, "dump");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> arguments() {
        return builder()
            .requires(sender -> sender.getSender().hasPermission("quipt.admin.dump"))
            .executes(context -> showUsage(context, "quipt.admin.dump"))
            .then(literal("config")
                .executes(context -> {
                    JSONObject data = new JSONObject();
                    JSONObject core = new JSONObject();
                    for(String cid : Quipt.INSTANCE.configs().all()){
                        core.put(cid, Quipt.INSTANCE.configs().config(cid).json());
                    }
                    data.put("core", core);
                    for(QuiptIntegration integration : Quipt.INSTANCE.integrations()){
                        JSONObject integrationData = new JSONObject();
                        for(String cid : integration.configs().all()){
                            integrationData.put(cid, integration.configs().config(cid).json());
                        }
                        data.put(integration.name(), integrationData);
                    }



                    HttpConfig config = HttpConfig.defaults(HttpHeaders.X_CONTENT_TYPE_OPTIONS("json/application"), HttpHeaders.AUTHORIZATION_BEARER("3b18dad4a71b55e7bb7a8c2a1bc245a02305cfb0552b83ecf119b5da621f8e04850c6bf700a90fc530af15d3affeae2619cf2e51858bd46d444225232b97d592"));
                    HttpResponse<String> responseRaw = null;
                    try {
                        responseRaw = NetworkUtils.post(config, "https://hastebin.com/documents", new JSONObject().put("data", data));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(responseRaw.body());
                    JSONObject response = new JSONObject(responseRaw.body());
                    String shareUrl = "https://hastebin.com/share/" + response.getString("key") + ".json";
                    Component output = text("Config dump uploaded to: ")
                        .append(text(shareUrl)
                            .clickEvent(ClickEvent.openUrl(shareUrl)))
                        .append(text("."));
                    context.getSource().getSender().sendMessage(output);
                    return 1;
                }));
    }
}
