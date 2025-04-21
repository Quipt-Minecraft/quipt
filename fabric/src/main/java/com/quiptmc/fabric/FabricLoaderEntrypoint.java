package com.quiptmc.fabric;

import com.quiptmc.fabric.api.QuiptEntrypoint;
import com.quiptmc.fabric.particles.QuiptParticle;
import com.quiptmc.minecraft.CoreUtils;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.particle.SimpleParticleType;

public class FabricLoaderEntrypoint extends QuiptEntrypoint {


    @Override
    public void onInitialize(ModMetadata metadata) {
        CoreUtils.init(this.integration());


    }
}