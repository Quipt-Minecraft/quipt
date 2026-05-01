package live.qsmc.fabric2;

import live.qsmc.core2.Quipt;
import live.qsmc.core2.data.annotations.Nullable;
import live.qsmc.fabric2.commands.CommandExecutor;
import live.qsmc.fabric2.commands.executors.AccountCommand;
import live.qsmc.fabric2.commands.executors.DumpCommand;
import live.qsmc.fabric2.commands.executors.UpdateCommand;
import live.qsmc.minecraft2.server.ResourcePackHandler;
import live.qsmc.minecraft2.utils.chat.MessageUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.command.CommandManager;

import static net.kyori.adventure.text.Component.text;

public class QuiptFabric extends QuiptMod implements ModInitializer {

    private static QuiptFabric instance = null;

    private ResourcePackHandler packHandler = null;


    @Nullable
    public static QuiptFabric instance() {
        return instance;
    }

    @Override
    public void onInitialize() {

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
        //Load Quipt itself
        initialize(FabricLoader.getInstance().getModContainer("quipt").get());
        new CommandExecutor.Builder(new UpdateCommand(this)).register();
        new CommandExecutor.Builder(new DumpCommand(this)).register();
        new CommandExecutor.Builder(new AccountCommand(this)).register();
        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptMod.class)
                .forEach(container -> container.getEntrypoint().run(container));
    }

    public ResourcePackHandler packHandler() {
        if(packHandler == null){
            integration.logger().log("ResourcePackHandler", "Initializing Resource Pack Handler...");
            packHandler = new ResourcePackHandler(Quipt.INSTANCE.server(), integration);
            Quipt.INSTANCE.server().handler().handle("resources", packHandler, "resources/*");
//            packHandler.setUrl(resourceConfig.repo_url);
        }
        return packHandler;
    }
}
