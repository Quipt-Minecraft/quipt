package com.quiptmc.fabric.particles;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class QuiptParticle {

    private final static com.quiptmc.core.data.registries.Registry<QuiptParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", QuiptParticleType.class);

    public static void register(String key, QuiptParticleType<?> type) {
        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type));
    }


    public static <T extends ParticleEffect> QuiptParticleType<T> get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }


}
