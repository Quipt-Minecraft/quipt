package live.qsmc.quipt.fabric.particles;


import net.minecraft.core.particles.ParticleType;

public abstract class QuiptParticleType<T extends QuiptParticleEffect> extends ParticleType<T> {

//    private final static live.qsmc.core.data.registries.Registry<QuiptParticleType> PARTICLE_TYPE_REGISTRY = live.qsmc.core.data.registries.Registries.register("particles", ()->null);
//
    protected QuiptParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }
//
//    public static void register(String key, QuiptParticleType<?> type) {
//        PARTICLE_TYPE_REGISTRY.register(key, Registry.register(Registries.PARTICLE_TYPE, Identifier.of("quipt", key), type));
//    }
//
//    public static <T extends QuiptParticleEffect> QuiptParticleType<T> get(String key) {
//        return PARTICLE_TYPE_REGISTRY.get(key).orElseThrow();
//    }
//
//    public static QuiptSimpleParticleType simple() {
//        return simple(false);
//    }
//
//    public static QuiptSimpleParticleType simple(boolean alwaysShow) {
//        return new QuiptSimpleParticleType(alwaysShow);
//    }
//
//    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(MapCodec<T> codec, PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<T, T> defaultEffect, T parameters) {
//        return complex(false, codec, packetCodec, defaultEffect, parameters);
//    }
//
//    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(boolean alwaysSpawn, final MapCodec<T> codec, final PacketCodec<? super RegistryByteBuf, T> packetCodec, Function<T, T> defaultEffect, T parameters) {
//        return new QuiptParticleType<T>(alwaysSpawn) {
//
//
//            @Override
//            public T effect(T parameters) {
//                return defaultEffect.apply(parameters);
//            }
//
//            public MapCodec<T> getCodec() {
//                return codec;
//            }
//
//            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
//                return packetCodec;
//            }
//        };
//    }
//
//    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(Function<QuiptParticleType<T>, MapCodec<T>> codecGetter, Function<QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter, Function<T, T> defaultEffect, T parameters) {
//        return complex(false, codecGetter, packetCodecGetter, defaultEffect, parameters);
//    }
//
//    public static <T extends QuiptParticleEffect> QuiptParticleType<T> complex(boolean alwaysSpawn, final Function<QuiptParticleType<T>, MapCodec<T>> codecGetter, final Function<QuiptParticleType<T>, PacketCodec<? super RegistryByteBuf, T>> packetCodecGetter, Function<T, T> defaultEffect, T parameters) {
//        return new QuiptParticleType<T>(alwaysSpawn) {
//
//
//            @Override
//            public T effect(T parameters) {
//                return defaultEffect.apply(parameters);
//            }
//
//            public MapCodec<T> getCodec() {
//                return codecGetter.apply(this);
//            }
//
//            public PacketCodec<? super RegistryByteBuf, T> getPacketCodec() {
//                return packetCodecGetter.apply(this);
//            }
//        };
//    }
//
//    public abstract T effect(T parameters);
}
