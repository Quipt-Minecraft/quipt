package com.quiptmc.fabric.client;

import com.quiptmc.fabric.blocks.QuiptBlocks;
import com.quiptmc.fabric.particles.QuiptParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.world.level.storage.LevelStorage;


public class EntrypointClient  implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(QuiptParticle.get("sparkle"), EndRodParticle.Factory::new);
    }
}
