package live.qsmc.paper2;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.data.annotations.Nullable;
import live.qsmc.minecraft2.config.files.ResourceConfig;
import live.qsmc.minecraft2.server.ResourcePackHandler;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import live.qsmc.paper2.api.players.PaperPlayers;
import live.qsmc.paper2.commands.CommandExecutor;
import live.qsmc.paper2.commands.executors.AccountCommand;
import live.qsmc.paper2.commands.executors.DumpCommand;
import live.qsmc.paper2.commands.executors.UpdateCommand;
import live.qsmc.paper2.commands.executors.WebhookCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

import static net.kyori.adventure.text.Component.text;

public class QuiptPaper extends QuiptPlugin {

    private PaperPlayers players = null;

    private ResourcePackHandler packHandler = null;

    private static QuiptPaper instance = null;

    @Nullable
    public static QuiptPaper instance() {
        return instance;
    }


    @Override
    public void enable() {
        instance = this;
        MessageUtils.init();
        MessageUtils.register(
            "cmd.update.usage",
            text("Usage: /update <group> <plugin> <build> <artifact>"));

        MessageUtils.register(
            "cmd.webhook.usage",
            text("Usage: /webhook <add|remove> <webhook> <url>"));

        MessageUtils.register(
            "cmd.account.usage",
            text("Usage: /account help"));

        MessageUtils.register(
            "cmd.account.help.usage",
            text("--------[Set up your Quipt Account]--------", NamedTextColor.GREEN)
                .appendNewline()
                .append(text("If you already have an account:", NamedTextColor.WHITE))
                .appendNewline()
                .append(text("    - You can run ", NamedTextColor.WHITE))
                .append(text("/account link <username> <password>", NamedTextColor.GRAY))
                .append(text(" to link your server to your Quipt account. OR", NamedTextColor.WHITE))
                .appendNewline()
                .append(text("    - You can run ", NamedTextColor.WHITE))
                .append(text("/account token <access_token>", NamedTextColor.GRAY))
                .append(text(" to limit what permissions this server has access to.", NamedTextColor.WHITE))
                .appendNewline()
                .appendNewline()
                .append(text("If you don't have an account yet:", NamedTextColor.WHITE))
                .appendNewline()
                .append(text("    - First run ", NamedTextColor.WHITE))
                .append(text("/account register <username> <password> <email>", NamedTextColor.GRAY))
                .appendNewline()
                .append(text("    - Then check your email for a verification link. You may need to check in your spam folder.", NamedTextColor.WHITE))
                .appendNewline()
                .append(text("    - After verifying your email, you can link your account using the command above.", NamedTextColor.WHITE))
                .appendNewline()
                .append(text("---------------------------------------", NamedTextColor.GREEN)));

        MessageUtils.register(
            "cmd.account.register.usage",
            text("Usage: /account register <username> <password> <email>"));


        MessageUtils.register(
            "quipt.account.register.success",
            text("Account registration has started. Please check your email for a verification link."));

        MessageUtils.register(
            "quipt.dump.success",
            "{\"color\":\"dark_green\",\"extra\":[{\"color\":\"green\",\"text\":\" has been uploaded successfully.\"},\"\\n\",{\"color\":\"green\",\"text\":\"Click \"},{\"color\":\"yellow\",\"click_event\":{\"action\":\"open_url\",\"url\":\"[1]\"},\"text\":\"here\"},{\"color\":\"green\",\"text\":\" to download.\"}],\"text\":\"[0]\"}");

        MessageUtils.save();

        if (integration().configs().config(ResourceConfig.class) == null)
            integration().configs().register(ResourceConfig.class);
        ResourceConfig config = integration().configs().config(ResourceConfig.class);
        if (config != null && config.enabled) {
            packHandler().start();
        }
        new CommandExecutor.Builder(new WebhookCommand(this)).setDescription("Alter webhooks").register();
        new CommandExecutor.Builder(new UpdateCommand(this)).setDescription("Update plugins from https://ci.qsmc.live").register();
        new CommandExecutor.Builder(new DumpCommand(this)).setDescription("Dump data to file").register();
        new CommandExecutor.Builder(new AccountCommand(this)).setDescription("Command for account management").register();
        integration().logger().log("Paper", "QuiptPaper for Paper enabled!");
    }

    public PaperPlayers players() {
        if (players == null) {
            integration().logger().log("Players", "Initializing players...");
            players = new PaperPlayers(integration());
        }
        return players;
    }

    public ResourcePackHandler packHandler() {
        if (packHandler == null) {
            integration.logger().log("ResourcePackHandler", "Initializing Resource Pack Handler...");
            packHandler = new ResourcePackHandler(Quipt.INSTANCE.server(), integration);
            Quipt.INSTANCE.server().handler().handle("resources", packHandler, "resources/*");
//            packHandler.setUrl(resourceConfig.repo_url);
        }
        return packHandler;
    }

}
