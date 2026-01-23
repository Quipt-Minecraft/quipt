package com.quiptmc2.minecraft.events.party;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigMap;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import com.quiptmc2.minecraft.api.MinecraftPlayer;
import com.quiptmc2.minecraft.config.files.PartyConfig;
import com.quiptmc2.minecraft.utils.chat.MessageUtils;
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

    public MinecraftIntegration<?> integration() {
        if(!(integration instanceof MinecraftIntegration))
            throw new IllegalStateException("Integration is not a MinecraftIntegration");
        return (MinecraftIntegration<?>) integration;
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
