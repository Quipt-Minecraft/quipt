package live.qsmc.quipt.fabric.players;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.platform.modcommon.impl.server.ServerPlayerAudience;
import org.json.JSONObject;

import java.util.UUID;

public class FabricPlayer extends MinecraftPlayer<ServerPlayerAudience> {


    public FabricPlayer(JSONObject json) {
        super(json);
    }

    @Deprecated
    public FabricPlayer(ServerPlayerAudience audience) {
        super(audience);
    }

    public FabricPlayer(QuiptIntegration integration, UUID uuid) {
        super(integration, uuid);
    }

    @Override
    public void teleport(MinecraftPlayer<ServerPlayerAudience> target) {

    }
}
