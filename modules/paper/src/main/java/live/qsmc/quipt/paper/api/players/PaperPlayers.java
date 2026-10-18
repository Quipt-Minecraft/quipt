package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperPlayers extends PlayerManager<JavaPlugin, NamespacedKey, Player, PaperPlayer> {


    public PaperPlayers(MinecraftIntegration<JavaPlugin, NamespacedKey> integration) {
        super(integration, PaperPlayer::new, PaperPlayer.class);
    }
}
