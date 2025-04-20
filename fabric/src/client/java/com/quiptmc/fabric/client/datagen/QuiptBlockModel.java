package com.quiptmc.fabric.client.datagen;


import net.minecraft.client.data.Model;
import net.minecraft.client.data.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class QuiptBlockModel extends Model {
    public QuiptBlockModel(Optional<Identifier> parent, Optional<String> variant, TextureKey... requiredTextureKeys) {
        super(parent, variant, requiredTextureKeys);
    }
}
