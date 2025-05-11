package com.quiptmc.fabric.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class QuiptParticle {

    private final static com.quiptmc.core.data.registries.Registry<ParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", ParticleType.class);

    public static void register(String key, ParticleType<?> type) {
        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type));
    }


    public static ParticleType<?> get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }


}
