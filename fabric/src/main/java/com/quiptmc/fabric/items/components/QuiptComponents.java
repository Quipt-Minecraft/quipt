package com.quiptmc.fabric.items.components;

import com.mojang.serialization.Codec;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import net.minecraft.component.ComponentType;
import net.minecraft.util.Identifier;

public class QuiptComponents {

    public static final Registry<ComponentType<?>> registry = Registries.register("components", ()->null);

    public static <T> void register(String modId, String path, Codec<T> codec) {
        ComponentType<T> componentType = net.minecraft.registry.Registry.register(
                net.minecraft.registry.Registries.DATA_COMPONENT_TYPE,
                Identifier.of(modId, path),
                ComponentType.<T>builder().codec(codec).build()
        );
        registry.register(modId + ":" + path, componentType);
    }

    public static <T> ComponentType<T> get(String modId, String path, Class<T> type) {
        return (ComponentType<T>) registry.get(modId + ":" + path).get();
    }



    public static class ComponentCodecType  {


    }
}
