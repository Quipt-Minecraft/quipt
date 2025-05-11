package com.quiptmc.fabric.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;

public class QuiptSimpleParticleType extends ParticleType<QuiptSimpleParticleType> implements ParticleEffect {
    private final MapCodec<QuiptSimpleParticleType> codec = MapCodec.unit(this::getType);
    private final PacketCodec<RegistryByteBuf, QuiptSimpleParticleType> packetCodec = PacketCodec.unit(this);

    protected QuiptSimpleParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public QuiptSimpleParticleType getType() {
        return this;
    }

    public MapCodec<QuiptSimpleParticleType> getCodec() {
        return this.codec;
    }

    public PacketCodec<RegistryByteBuf, QuiptSimpleParticleType> getPacketCodec() {
        return this.packetCodec;
    }
}
