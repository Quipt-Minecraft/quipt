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
import net.minecraft.server.command.CommandManager;

public class QuiptFabric extends QuiptMod implements ModInitializer {

    private static QuiptFabric instance = null;

    private ResourcePackHandler packHandler = null;


    @Nullable
    public static QuiptFabric instance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        MessageUtils.init();
        instance = this;
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
