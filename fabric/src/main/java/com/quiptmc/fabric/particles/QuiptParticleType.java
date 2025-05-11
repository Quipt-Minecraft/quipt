package com.quiptmc.fabric.particles;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class QuiptParticleType<T extends ParticleEffect> extends ParticleType<T> {

    private final static com.quiptmc.core.data.registries.Registry<com.quiptmc.fabric.particles.QuiptParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", com.quiptmc.fabric.particles.QuiptParticleType.class);

    protected QuiptParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public static void register(String key, com.quiptmc.fabric.particles.QuiptParticleType<?> type) {
        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type));
    }

    public static <T extends ParticleEffect> com.quiptmc.fabric.particles.QuiptParticleType<T> get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }

    public static QuiptSimpleParticleType simple() {
        return simple(false);
    }

    public static QuiptSimpleParticleType simple(boolean alwaysShow) {
        return new QuiptSimpleParticleType(alwaysShow);
    }

    public static <T extends ParticleEffect, R> com.quiptmc.fabric.particles.QuiptParticleType<T> complex(MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<R, T> defaultEffect, R r) {
        return complex(false, codec, packetCodec, defaultEffect, r);
    }

    public static <T extends ParticleEffect, R> com.quiptmc.fabric.particles.QuiptParticleType<T> complex(boolean alwaysSpawn, final MapCodec<T> codec, final PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<R, T> defaultEffect, R r) {
        return new com.quiptmc.fabric.particles.QuiptParticleType<T>(alwaysSpawn) {
            @Override
            public T effect() {
                return defaultEffect.apply(r);
            }

            public MapCodec<T> getCodec() {
                return codec;
            }

            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return packetCodec;
            }
        };
    }

    public static <T extends ParticleEffect> com.quiptmc.fabric.particles.QuiptParticleType<T> complex(Function<com.quiptmc.fabric.particles.QuiptParticleType<T>, MapCodec<T>> codecGetter, Function<com.quiptmc.fabric.particles.QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter) {
        return complex(false, codecGetter, packetCodecGetter);
    }

    public static <T extends ParticleEffect> com.quiptmc.fabric.particles.QuiptParticleType<T> complex(boolean alwaysSpawn, final Function<com.quiptmc.fabric.particles.QuiptParticleType<T>, MapCodec<T>> codecGetter, final Function<com.quiptmc.fabric.particles.QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter) {
        return new com.quiptmc.fabric.particles.QuiptParticleType<T>(alwaysSpawn) {
            @Override
            public T effect() {
                return null;
            }

            public MapCodec<T> getCodec() {
                return (MapCodec)codecGetter.apply(this);
            }

            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return (PacketCodec)packetCodecGetter.apply(this);
            }
        };
    }

    public abstract T effect();
}
