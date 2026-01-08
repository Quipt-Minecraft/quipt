package com.quiptmc.minecraft.api.statistics;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;

public class MinecraftStats {

    public static final Registry<MinecraftStat> registry = Registries.register("stats", ()->null);


}
