package live.qsmc.fabric2;

import live.qsmc.fabric2.api.QuiptMod;
import net.fabricmc.loader.api.FabricLoader;

public class Quipt extends QuiptMod {

    @Override
    public void onInitialize() {
        //Load Quipt itself
        run(FabricLoader.getInstance().getModContainer("quipt").get());
        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptMod.class)
                .forEach(container -> container.getEntrypoint().run(container));
    }
}
