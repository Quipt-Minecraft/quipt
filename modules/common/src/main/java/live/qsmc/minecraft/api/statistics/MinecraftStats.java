package live.qsmc.minecraft.api.statistics;

import live.qsmc.core.data.registries.Registries;
import live.qsmc.core.data.registries.Registry;

public class MinecraftStats {

    public static final Registry<MinecraftStat> registry = Registries.register("stats", ()->null);


}
