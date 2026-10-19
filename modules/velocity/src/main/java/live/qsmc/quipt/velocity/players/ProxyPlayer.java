package live.qsmc.quipt.velocity.players;

import com.velocitypowered.api.proxy.Player;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import net.kyori.adventure.audience.Audience;
import org.json.JSONObject;

import java.util.UUID;

public class ProxyPlayer extends MinecraftPlayer<Player> {


    public ProxyPlayer(QuiptIntegration integration, UUID uuid) {
        super(integration, uuid);
    }

    public ProxyPlayer(Player audience) {
        super(audience);
    }

    @Deprecated
    public ProxyPlayer(JSONObject json) {
        super(json);
    }

    @Override
    public void teleport(MinecraftPlayer<Player> target) {

    }
}
