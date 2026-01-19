package com.quiptmc2.minecraft.api;

import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.statistics.MinecraftStat;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

import java.util.UUID;

public abstract class MinecraftPlayer extends ConfigObject implements Audience {

    public MinecraftPlayer(QuiptIntegration integration) {
        super(integration);
    }

    public abstract int getStatistic(MinecraftStat stat);
    public abstract int getStatistic(MinecraftStat stat, MinecraftMaterial material);
    public abstract int getStatistic(MinecraftStat stat, MinecraftEntityType entity);
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
