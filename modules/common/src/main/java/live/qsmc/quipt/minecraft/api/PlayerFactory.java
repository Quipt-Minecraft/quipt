package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.QuiptIntegration;
import net.kyori.adventure.audience.Audience;

public abstract class PlayerFactory<P extends Audience, O extends MinecraftPlayer> {

    MinecraftIntegration<?, ?> integration;

    public PlayerFactory(MinecraftIntegration<?, ?> integration){
        this.integration = integration;
    }

    public abstract O apply(P player);
}
