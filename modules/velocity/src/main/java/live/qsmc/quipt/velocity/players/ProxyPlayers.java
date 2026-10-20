package live.qsmc.quipt.velocity.players;

import com.velocitypowered.api.proxy.Player;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import org.json.JSONObject;

import java.util.function.Function;

public class ProxyPlayers extends PlayerManager<Player, ProxyPlayer> {
    public ProxyPlayers(QuiptIntegration integration) {
        super(integration, ProxyPlayer::new, new ConfigObject.Factory<ProxyPlayer>() {
            @Override
            public String getClassName() {
                return ProxyPlayer.class.getName();
            }

            @Override
            @Deprecated
            public ProxyPlayer createFromJson(QuiptIntegration integration, JSONObject json) {
                ProxyPlayer player = new ProxyPlayer(json);
                return player;
            }
        });
    }
}
