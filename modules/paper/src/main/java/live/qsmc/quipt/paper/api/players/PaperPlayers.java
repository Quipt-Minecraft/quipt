package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PaperPlayers {

    private final Registry<PaperPlayer> players;
    private final MinecraftIntegration<JavaPlugin, NamespacedKey> integration;

    public PaperPlayers(MinecraftIntegration<JavaPlugin, NamespacedKey> integration){
        players = Quipt.INSTANCE.registries().register("players", ()->null);
        this.integration = integration;
    }

    public PaperPlayer of(Player player){
        return players.get(player.getUniqueId().toString()).orElse(register(player));
    }

    private PaperPlayer register(Player player){
        PaperPlayer playerData = new PaperPlayer(integration, player);
        players.register(player.getUniqueId().toString(), playerData);
        return playerData;
    }
}
