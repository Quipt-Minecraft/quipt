package live.qsmc.minecraft.api;

import live.qsmc.minecraft.api.statistics.MinecraftStat;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public interface MinecraftPlayer extends Audience {
    int getStatistic(MinecraftStat stat);

    int getStatistic(MinecraftStat stat, MinecraftMaterial material);
    int getStatistic(MinecraftStat stat, MinecraftEntityType entity);
    void teleport(MinecraftPlayer target);

    default Component name(){
        return get(Identity.DISPLAY_NAME).orElse(Component.text(getName()));
    }

    default String getName() {
        return null;
    }

    default UUID uuid(){
        return get(Identity.UUID).orElseThrow();
    }
}
