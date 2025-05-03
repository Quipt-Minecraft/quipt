package com.quiptmc.fabric;

import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.listeners.ServerListener;
import com.quiptmc.fabric.particles.QuiptParticle;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.particle.SimpleParticleType;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize(ModMetadata metadata) {
        CoreUtils.init(this.integration());
        MinecraftMaterial.init();
        Registry<MinecraftMaterial> materialRegistry = MinecraftMaterial.registry().orElseThrow();
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