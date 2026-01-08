package com.quiptmc.fabric.items.abstracts;

import net.minecraft.item.Item;

public abstract class QuiptNormalItem extends Item implements QuiptItem {


    public QuiptNormalItem(Settings settings) {
        super(settings);
    }

    @Override
    public Item item() {
        return this;
    }
}
