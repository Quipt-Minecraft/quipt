package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.minecraft.MinecraftIntegration;
import net.kyori.adventure.audience.Audience;

public abstract class PlayerFactory<P extends Audience, O extends MinecraftPlayer> {

    MinecraftIntegration<?, ?> integration;

    public PlayerFactory(MinecraftIntegration<?, ?> integration){
        this.integration = integration;
    }

    public abstract O apply(P player);
}
