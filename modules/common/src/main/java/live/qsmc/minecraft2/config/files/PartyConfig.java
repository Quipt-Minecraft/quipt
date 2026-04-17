package live.qsmc.minecraft2.config.files;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.Config;
import live.qsmc.core2.config.ConfigTemplate;
import live.qsmc.core2.config.ConfigValue;
import live.qsmc.core2.config.objects.ConfigMap;
import live.qsmc.minecraft2.api.MinecraftPlayer;
import live.qsmc.minecraft2.events.party.Party;
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