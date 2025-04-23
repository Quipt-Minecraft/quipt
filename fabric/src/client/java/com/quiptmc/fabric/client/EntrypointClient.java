package com.quiptmc.fabric.client;

import com.quiptmc.fabric.blocks.QuiptBlocks;
import com.quiptmc.fabric.particles.QuiptParticle;
import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.minecraft.api.ApiManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ConnectionParticle;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.world.level.storage.LevelStorage;


public class EntrypointClient  implements ClientModInitializer {

    ApiManager apiManager;

    @Override
    public void onInitializeClient() {
        apiManager = new ApiManager(CoreUtils.integration());
    }
}
