package com.quiptmc.fabric;

import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.listeners.ServerListener;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class Initializer extends QuiptEntrypoint implements ModInitializer   {



    @Override
    public void onInitialize() {
        //Load Quipt data
        CoreUtils.init(this.integration());
        MinecraftMaterial.init();
        Registry<MinecraftMaterial> materialRegistry = MinecraftMaterial.registry().orElseThrow();



        //Load other Quipt mods
        FabricLoader.getInstance().getEntrypointContainers("quipt", QuiptEntrypoint.class).forEach(container -> container.getEntrypoint().run(container));
    }


    @Override
    public void onInitialize(ModMetadata metadata) {
        //todo register materials?
        ServerListener mainListener = new ServerListener();
        ServerLifecycleEvents.SERVER_STOPPING.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTED.register(mainListener);
        ServerLifecycleEvents.SERVER_STARTING.register(mainListener);
        ServerPlayConnectionEvents.JOIN.register(mainListener);
        ServerPlayConnectionEvents.DISCONNECT.register(mainListener);
        ServerMessageEvents.CHAT_MESSAGE.register(mainListener);
    }
}
