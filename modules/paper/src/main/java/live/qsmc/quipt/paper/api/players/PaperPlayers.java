package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import org.bukkit.entity.Player;
import org.json.JSONObject;

public class PaperPlayers extends PlayerManager<Player, PaperPlayer> {
    public PaperPlayers(QuiptIntegration integration) {
        super(integration, PaperPlayer::new, new ConfigObject.Factory<>() {
            @Override
            public String getClassName() {
                return PaperPlayer.class.getName();
            }

            @Override
            @Deprecated
            public PaperPlayer createFromJson(QuiptIntegration integration, JSONObject json) {
                PaperPlayer player = new PaperPlayer(json);
                return player;
            }
        });
    }
}
