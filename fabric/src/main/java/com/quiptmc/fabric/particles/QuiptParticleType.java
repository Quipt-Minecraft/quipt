package com.quiptmc.fabric.particles;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;

public abstract class QuiptParticleType<T extends ParticleEffect> extends ParticleType<T>  {

    public static QuiptSimpleParticleType simple(){
        return new QuiptSimpleParticleType(false);

    }
    protected QuiptParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public abstract T effect();
}
