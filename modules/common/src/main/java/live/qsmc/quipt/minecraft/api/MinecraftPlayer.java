package live.qsmc.quipt.minecraft.api;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.core.data.Metadata;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.json.JSONObject;

import java.util.UUID;

public abstract class MinecraftPlayer extends ConfigObject implements Audience {

    public Metadata metadata = new Metadata();

    public MinecraftPlayer(QuiptIntegration integration, UUID uuid) {
        super(integration);
        super.id = uuid.toString();
    }

    @Deprecated
    public MinecraftPlayer(JSONObject json) {
        super(Quipt.INSTANCE, json);
        if(json.has("metadata")){
            this.metadata = Metadata.of(json.getJSONObject("metadata"));
        }
    }


//    public abstract int getStatistic(MinecraftStat stat);
//    public abstract int getStatistic(MinecraftStat stat, MinecraftMaterial material);
//    public abstract int getStatistic(MinecraftStat stat, MinecraftEntityType entity);
    public abstract void teleport(MinecraftPlayer target);


    public Component name(){
        return get(Identity.DISPLAY_NAME).orElse(Component.text(getName()));
    }

    public String getName() {
        return null;
    }

    public UUID uuid(){
        return get(Identity.UUID).orElseThrow();
    }

}
