package com.quiptmc.fabric.particles;

import com.mojang.serialization.MapCodec;
import com.quiptmc.fabric.particles.QuiptParticleType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.particle.ParticleEffect;

public class QuiptSimpleParticleType extends QuiptParticleType<QuiptSimpleParticleType> implements QuiptParticleEffect {
    private final MapCodec<QuiptSimpleParticleType> codec = MapCodec.unit(this::getType);
    private final PacketCodec<RegistryByteBuf, QuiptSimpleParticleType> packetCodec = PacketCodec.unit(this);

    public QuiptSimpleParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    @Override
    public QuiptSimpleParticleType effect(QuiptSimpleParticleType parameters) {
        return this;
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

    @Override
    public float getScale() {
        return 1f;
    }
}
