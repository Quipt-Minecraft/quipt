package live.qsmc.minecraft2.api;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.core2.config.objects.ConfigObject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public abstract class MinecraftPlayer extends ConfigObject implements Audience {

    public MinecraftPlayer(QuiptIntegration integration, UUID uuid) {
        super(integration);
        super.id = uuid.toString();
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
