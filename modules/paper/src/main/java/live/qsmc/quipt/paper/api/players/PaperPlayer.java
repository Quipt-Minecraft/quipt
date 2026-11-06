package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.data.Metadata;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.JSONObject;
//import com.quiptmc2.paper.api.settings.Settings;

import java.util.UUID;

public class PaperPlayer extends MinecraftPlayer<Player> {

    public long last_seen;
    /**
     * @deprecated Use {@link #PaperPlayer(org.bukkit.entity.Player)} instead.
     * @param json Raw data
     */
    @Deprecated
    public PaperPlayer(JSONObject json) {
        super(json);
        super.metadata = Metadata.of(json.optJSONObject("metadata", new JSONObject()));
    }

    public PaperPlayer(org.bukkit.entity.Player player) {
        super(Quipt.INSTANCE, player.getUniqueId());
        this.last_seen = System.currentTimeMillis();
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
    }

    @Override
    public UUID uuid() {
        return UUID.fromString(id);
    }

    public long lastSeen() {
        return last_seen;
    }

    public OfflinePlayer player() {
        return Bukkit.getOfflinePlayer(UUID.fromString(id));
    }

//    @Override
//    public int getStatistic(MinecraftStat stat) {
//        return 0;
//    }

//    @Override
//    public int getStatistic(MinecraftStat stat, MinecraftMaterial material) {
//        return 0;
//    }

//    @Override
//    public int getStatistic(MinecraftStat stat, MinecraftEntityType entity) {
//        return 0;
//    }

    @Override
    public void teleport(MinecraftPlayer target) {
        //todo change return to TeleportRequest and set up Request system
        player().getPlayer().teleport(((PaperPlayer) target).player().getPlayer());
    }





}