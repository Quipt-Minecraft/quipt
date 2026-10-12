package live.qsmc.quipt.fabric.particles;

import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

public abstract class AbstractQuiptParticle<T extends QuiptParticleEffect> extends SingleQuadParticle {
    private final SpriteSet SpriteSet;

    protected AbstractQuiptParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, T parameters, SpriteSet SpriteSet) {
        super(world, x, y, z, velocityX, velocityY, velocityZ, SpriteSet.first());
        this.friction = 0.96F;
        this.speedUpWhenYMotionIsBlocked = true;
        this.SpriteSet = SpriteSet;
        this.xd *= 0.1F;
        this.yd *= 0.1F;
        this.zd *= 0.1F;
        this.quadSize *= 0.75F * parameters.getScale();
        int i = (int)((double)8.0F / (this.random.nextDouble() * 0.8 + 0.2));
        this.lifetime = (int)Math.max((float)i * parameters.getScale(), 1.0F);
        this.setSpriteFromAge(SpriteSet);
    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
    }

    public SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.OPAQUE;
    }

    public float getSize(float tickProgress) {
        return this.quadSize * Mth.clamp(((float)this.age + tickProgress) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.SpriteSet);
    }
}

//protected AbstractQuiptParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, T parameters, SpriteSet SpriteSet) {
//        super(world, x, y, z, velocityX, velocityY, velocityZ);
//        this.velocityMultiplier = 0.96F;
//        this.ascending = true;
//        this.SpriteSet = SpriteSet;
//        this.velocityX *= (double)0.1F;
//        this.velocityY *= (double)0.1F;
//        this.velocityZ *= (double)0.1F;
//        this.scale *= 0.75F * parameters.getScale();
//        int i = (int)((double)8.0F / (this.random.nextDouble() * 0.8 + 0.2));
//        this.maxAge = (int)Math.max((float)i * parameters.getScale(), 1.0F);
//        this.setSpriteForAge(SpriteSet);
//    }
//
//    protected float darken(float colorComponent, float multiplier) {
//        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
//    }
//
//    public ParticleTextureSheet getType() {
//        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
//    }
//
//    public float getSize(float tickDelta) {
//        return this.scale * Mth.clamp(((float)this.age + tickDelta) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
//    }
//}
