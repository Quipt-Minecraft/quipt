package com.quiptmc.fabric.blocks.abstracts.properties;

import net.minecraft.state.property.Property;

import java.util.List;
import java.util.Optional;

public abstract class BlockProperty<T extends Comparable<T>> extends Property<T> {

    T defaultValue;
    protected BlockProperty(String name, T defaultValue, Class<T> type) {
        super(name, type);
        this.defaultValue = defaultValue;
    }

    public T defaultValue(){
        return defaultValue;
    }
}
