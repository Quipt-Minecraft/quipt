package com.quiptmc.fabric.blocks.abstracts.properties;

import net.minecraft.state.property.Property;

import java.util.List;
import java.util.Optional;

public abstract class BlockProperty<T extends Comparable<T>> extends Property<T> {

    protected BlockProperty(String name, Class<T> type) {
        super(name, type);
    }
}
