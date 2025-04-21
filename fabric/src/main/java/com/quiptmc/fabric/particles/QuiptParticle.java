package com.quiptmc.fabric.particles;

import com.quiptmc.minecraft.CoreUtils;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class QuiptParticle {

    private final static com.quiptmc.core.data.registries.Registry<ParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", ParticleType.class);

    public static void register(String key, ParticleType<? extends ParticleEffect> type) {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type);
        PARTICLE_TYPE_REGISTRY.register(key, type);
    }


    public static <T extends ParticleEffect> ParticleType<T> get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }


}
