package com.quiptmc.minecraft.utils.chat.placeholder;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.minecraft.api.MinecraftPlayer;
//import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map.Entry;
import java.util.Optional;

import static com.quiptmc.minecraft.utils.chat.MessageUtils.deserialize;
import static com.quiptmc.minecraft.utils.chat.MessageUtils.serialize;

public class PlaceholderUtils {

    private static final Registry<Placeholder> registry = Registries.register("placeholders", ()->null);

    public static void registerPlaceholders() {
        registerPlaceholder("name", player -> player.map(audience -> serialize(audience.get(Identity.DISPLAY_NAME).orElse(Component.text("Unknown")))).orElse("Unknown"));
//        registerPlaceholder("online", (player) -> Bukkit.getOnlinePlayers().size() + "");
    }

    public static void registerPlaceholder(String key, Placeholder worker) {
        registry.register("%" + key + "%", worker);
    }

    public static Component replace(@Nullable MinecraftPlayer player, Component component) {
        return component.replaceText(builder -> {
            for (Entry<String, Placeholder> e : registry.toMap().entrySet()) {
                builder.match(e.getKey()).replacement(e.getValue().run(Optional.ofNullable(player)));
            }
        });
    }

    public static String replace(@Nullable MinecraftPlayer player, String string) {

        for (Entry<String, Placeholder> e : registry.toMap().entrySet()) {
            if (string.contains(e.getKey())) {
                string = string.replaceAll(e.getKey(), e.getValue().run(Optional.ofNullable(player)));
            }
        }

        string = emotify(string);
        return string;
    }

    public static String emotify(String string) {
        String tag = string;
        while (tag.contains("%symbol:")) {
            String icon = tag.split("ymbol:")[1].split("%")[0];
            if (Symbols.valueOf(icon.toUpperCase()) == null) {
                tag = tag.replaceAll("%symbol:" + icon + "%", Symbols.UNKNOWN.toString());
            } else {
                tag = tag.replaceAll("%symbol:" + icon + "%", Symbols.valueOf(icon.toUpperCase()).toString());
            }
        }
        return tag;
    }

    @FunctionalInterface
    public interface Placeholder {
        String run(Optional<MinecraftPlayer> player);
    }
}