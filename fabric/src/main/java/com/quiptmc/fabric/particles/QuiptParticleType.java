package com.quiptmc.fabric.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public abstract class QuiptParticleType<T extends QuiptParticleEffect> extends ParticleType<T> {

    private final static com.quiptmc.core.data.registries.Registry<QuiptParticleType> PARTICLE_TYPE_REGISTRY = com.quiptmc.core.data.registries.Registries.register("particles", ()->null);

    protected QuiptParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public static void register(String key, QuiptParticleType<?> type) {
        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type));
    }

    public static <T extends QuiptParticleEffect> QuiptParticleType<T> get(String key) {
        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
    }

    public static QuiptSimpleParticleType simple() {
        return simple(false);
    }

    public static QuiptSimpleParticleType simple(boolean alwaysShow) {
        return new QuiptSimpleParticleType(alwaysShow);
    }

    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<T, T> defaultEffect, T parameters) {
        return complex(false, codec, packetCodec, defaultEffect, parameters);
    }

    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(boolean alwaysSpawn, final MapCodec<T> codec, final PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<T, T> defaultEffect, T parameters) {
        return new QuiptParticleType<T>(alwaysSpawn) {


            @Override
            public T effect(T parameters) {
                return defaultEffect.apply(parameters);
            }

            public MapCodec<T> getCodec() {
                return codec;
            }

            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return packetCodec;
            }
        };
    }

    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(Function<QuiptParticleType<T>, MapCodec<T>> codecGetter, Function<QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter, Function<T, T> defaultEffect, T parameters) {
        return complex(false, codecGetter, packetCodecGetter, defaultEffect, parameters);
    }

    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(boolean alwaysSpawn, final Function<QuiptParticleType<T>, MapCodec<T>> codecGetter, final Function<QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter, Function<T, T> defaultEffect, T parameters) {
        return new QuiptParticleType<T>(alwaysSpawn) {


            @Override
            public T effect(T parameters) {
                return defaultEffect.apply(parameters);
            }

            public MapCodec<T> getCodec() {
                return codecGetter.apply(this);
            }

            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
                return packetCodecGetter.apply(this);
            }
        };
    }

    public abstract T effect(T parameters);
}
