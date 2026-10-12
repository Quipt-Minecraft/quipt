package live.qsmc.quipt.fabric.particles;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class QuiptSimpleParticleType extends QuiptParticleType<QuiptSimpleParticleType> implements QuiptParticleEffect {
    private final MapCodec<QuiptSimpleParticleType> codec = MapCodec.unit(this::getType);
    private final StreamCodec<RegistryFriendlyByteBuf, QuiptSimpleParticleType> streamCodec = StreamCodec.unit(this);

    public QuiptSimpleParticleType(boolean alwaysShow) {
        super(alwaysShow);
    }

//    @Override
//    public QuiptSimpleParticleType effect(QuiptSimpleParticleType parameters) {
//        return this;
//    }


    public QuiptSimpleParticleType getType() {
        return this;
    }

    public MapCodec<QuiptSimpleParticleType> codec() {
        return this.codec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, QuiptSimpleParticleType> streamCodec() {
        return this.streamCodec;
    }

    @Override
    public float getScale() {
        return 1f;
    }
}
