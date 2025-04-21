package com.quiptmc.fabric.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class QuiptParticle {

    private final static com.quiptmc.core.data.registries.Registry<SimpleParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", SimpleParticleType.class);

    public static void register(String key) {
        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), FabricParticleTypes.simple()));
    }


    public static SimpleParticleType get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }


}
