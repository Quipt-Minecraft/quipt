package com.quiptmc.paper.entity.entities;

import com.quiptmc.paper.entity.QuiptEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.breeze.Breeze;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class QuiptBreeze extends Breeze implements QuiptEntity<Breeze> {
    public QuiptBreeze(Location location) {
        super(EntityType.BREEZE, ((CraftWorld) location.getWorld()).getHandle());
        spawn(location,this);

    }
}