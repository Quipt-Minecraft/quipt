package live.qsmc.quipt.paper.api.players;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import org.bukkit.Bukkit;
import org.json.JSONObject;
//import com.quiptmc2.paper.api.settings.Settings;

import java.util.UUID;

public class PaperPlayer extends MinecraftPlayer {

    public long last_seen;
//    public Settings settings;

    private org.bukkit.entity.Player player;


    /**
     * @deprecated Use {@link #PaperPlayer(org.bukkit.entity.Player)} instead.
     * @param json Raw data
     */
    @Deprecated
    public PaperPlayer(JSONObject json) {
        super(json);
        this.player = Bukkit.getServer().getPlayer(UUID.fromString(id));
    }

    public PaperPlayer(org.bukkit.entity.Player player) {
        super(Quipt.INSTANCE, player.getUniqueId());
        this.player = player;
        this.last_seen = System.currentTimeMillis();
    }

    @Override
    public void fromJson(JSONObject json) {
        super.fromJson(json);
        this.player = Bukkit.getServer().getPlayer(UUID.fromString(id));
    }

    @Override
    public UUID uuid() {
        return UUID.fromString(id);
    }

    public long lastSeen() {
        return last_seen;
    }

    public org.bukkit.entity.Player player() {
        return player;
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
        player().teleport(((PaperPlayer) target).player());
    }





}