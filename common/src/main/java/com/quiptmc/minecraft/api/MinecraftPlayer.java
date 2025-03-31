package com.quiptmc.minecraft.api;

import com.quiptmc.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;

public interface MinecraftPlayer extends Audience {
    int getStatistic(MinecraftStatistic stat);

    int getStatistic(MinecraftStatistic stat, MinecraftMaterial material);

    int getStatistic(MinecraftStatistic stat, MinecraftEntityType entity);

    void teleport(MinecraftPlayer target);

    default String getName() {
        return MessageUtils.plainText(get(Identity.DISPLAY_NAME).orElseThrow());
    }

    default Component name(){
        return get(Identity.DISPLAY_NAME).orElseThrow();
    }
}
