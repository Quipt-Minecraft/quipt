package com.quiptmc.fabric.items.abstracts;

import net.minecraft.item.Item;

public class QuiptFoodItem extends QuiptNormalItem {

    private final FoodSettings foodSettings;

    public QuiptFoodItem(Item.Settings settings, FoodSettings foodSettings) {
        super(settings);
        this.foodSettings = foodSettings;
    }


}
