package live.qsmc.quipt.fabric.players;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.MinecraftIntegration;
import live.qsmc.quipt.minecraft.api.PlayerManager;
import net.fabricmc.loader.api.ModContainer;
import net.kyori.adventure.platform.modcommon.impl.server.ServerPlayerAudience;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.json.JSONObject;

import java.util.function.Function;

public class FabricPlayers extends PlayerManager<ServerPlayerAudience, FabricPlayer> {


    public FabricPlayers(QuiptIntegration integration) {
        super(integration, FabricPlayer::new, new ConfigObject.Factory<>() {
            @Override
            public String getClassName() {
                return FabricPlayer.class.getName();
            }

            @Override
            @Deprecated
            public FabricPlayer createFromJson(QuiptIntegration integration, JSONObject json) {
                FabricPlayer player = new FabricPlayer(json);
                return player;
            }
        });
    }
}
