package com.quiptmc2.minecraft.config.files;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.Config;
import com.quiptmc2.core.config.ConfigTemplate;
import com.quiptmc2.core.config.ConfigValue;
import com.quiptmc2.core.config.objects.ConfigMap;
import com.quiptmc2.minecraft.api.MinecraftPlayer;
import com.quiptmc2.minecraft.events.party.Party;
import org.json.JSONObject;

import java.io.File;
import java.util.Optional;

@ConfigTemplate(name = "party", ext = ConfigTemplate.Extension.JSON)
public class PartyConfig extends Config {

    @ConfigValue
    public ConfigMap<Party> parties;

    /**
     * Creates a new config file
     *
     * @param file        The file to save to
     * @param name        The name of the config
     * @param extension   The extension of the config
     * @param integration The plugin that owns this config
     */
    public PartyConfig(File file, String name, ConfigTemplate.Extension extension, QuiptIntegration integration) {
        super(file, name, extension, integration);
        parties = new ConfigMap<>(integration);
    }

    public Party create(String name){
        if(parties.contains(name)) return get(name);
        JSONObject partyData = new JSONObject();
        partyData.put("id", name);
        Party party = new Party(integration(), partyData);
        parties.put(party);
        save();
        return party;
    }

    public Party get(String name){
        if(!parties.contains(name)) return null;
        return parties.get(name);
    }

    public Optional<Party> get(MinecraftPlayer player){
        for(Party party : parties.values()){
            if(party.members.contains(player.uuid().toString())){
                return Optional.of(party);
            }
        }
        return Optional.empty();
    }

    public void remove(Party party) {
        parties.remove(party.id());
        save();
    }
}