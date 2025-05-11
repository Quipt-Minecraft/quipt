package com.quiptmc.fabric.particles;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

public abstract class QuiptParticleType<T extends ParticleEffect> extends ParticleType<T>  {
    protected QuiptParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

    public abstract T effect();
}
