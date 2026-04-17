package live.qsmc.fabric2;

import live.qsmc.core2.data.annotations.Nullable;
import live.qsmc.fabric2.commands.CommandExecutor;
import live.qsmc.fabric2.commands.executors.UpdateCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.CommandManager;

public class QuiptFabric extends QuiptMod implements ModInitializer {

    private static QuiptFabric instance = null;

    @Nullable
    public static QuiptFabric instance() {
        return instance;
    }

    @Override
    public void onInitialize() {
        instance = this;
        //Load Quipt itself
        initialize(FabricLoader.getInstance().getModContainer("quipt").get());
        new CommandExecutor.Builder(new UpdateCommand(this)).register();
        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptMod.class)
                .forEach(container -> container.getEntrypoint().run(container));
    }
}
