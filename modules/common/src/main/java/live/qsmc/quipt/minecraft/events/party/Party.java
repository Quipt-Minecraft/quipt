package live.qsmc.quipt.minecraft.events.party;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigMap;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.minecraft.api.MinecraftIntegration;
import live.qsmc.quipt.minecraft.api.MinecraftPlayer;
import live.qsmc.quipt.minecraft.config.files.PartyConfig;
import live.qsmc.quipt.minecraft.utils.chat.MessageUtils;
import org.json.JSONObject;

import java.util.Optional;

public class Party extends ConfigObject {


    public ConfigMap<MinecraftPlayer> members;

    public Party(QuiptIntegration integration, JSONObject json) {
        this(integration);
        this.fromJson(json);
    }

    public Party(QuiptIntegration integration) {
        super(integration);
        members = new ConfigMap<>(integration);
    }

    public MinecraftIntegration<?,?> integration() {
        if(!(integration instanceof MinecraftIntegration))
            throw new IllegalStateException("Integration is not a MinecraftIntegration");
        return (MinecraftIntegration<?,?>) integration;
    }


    public void join(MinecraftPlayer player) {

        PartyConfig config = integration().parties();
        Optional<Party> previousParty = config.get(player);
        previousParty.ifPresent(party -> party.leave(player));

        members.put(player);
        config.save();
        player.sendMessage(MessageUtils.get("lastlife.party.join", this.id));
    }

    public void leave(MinecraftPlayer player) {
        members.remove(player);
        integration().parties().save();
        player.sendMessage(MessageUtils.get("lastlife.party.leave", this.id));

    }
}
