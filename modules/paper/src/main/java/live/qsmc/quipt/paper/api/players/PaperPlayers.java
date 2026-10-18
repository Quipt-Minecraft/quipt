package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;

public class PaperPlayers extends PlayerManager<JavaPlugin, NamespacedKey, Player, PaperPlayer> {


    public PaperPlayers(MinecraftIntegration<JavaPlugin, NamespacedKey> integration) {
        super(integration, PaperPlayer::new, new ConfigObject.Factory<>() {
            @Override
            public String getClassName() {
                return PaperPlayer.class.getName();
            }

            @Override
            public PaperPlayer createFromJson(QuiptIntegration integration, JSONObject json) {
                return new PaperPlayer(json);
            }
        });
    }
}
