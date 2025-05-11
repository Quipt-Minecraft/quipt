package com.quiptmc.fabric.particles;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

public abstract class AbstractQuiptParticle<T extends QuiptParticleEffect> extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;

    protected AbstractQuiptParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, T parameters, SpriteProvider spriteProvider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.96F;
        this.ascending = true;
        this.spriteProvider = spriteProvider;
        this.velocityX *= (double)0.1F;
        this.velocityY *= (double)0.1F;
        this.velocityZ *= (double)0.1F;
        this.scale *= 0.75F * parameters.getScale();
        int i = (int)((double)8.0F / (this.random.nextDouble() * 0.8 + 0.2));
        this.maxAge = (int)Math.max((float)i * parameters.getScale(), 1.0F);
        this.setSpriteForAge(spriteProvider);
    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp(((float)this.age + tickDelta) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }
}
