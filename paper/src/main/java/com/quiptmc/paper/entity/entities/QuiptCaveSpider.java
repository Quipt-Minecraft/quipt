package com.quiptmc.paper.entity.entities;

import com.quiptmc.paper.entity.QuiptEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class QuiptCaveSpider extends CaveSpider implements QuiptEntity<CaveSpider> {
    public QuiptCaveSpider(Location location) {
        super(EntityType.CAVE_SPIDER, ((CraftWorld) location.getWorld()).getHandle());
        spawn(location,this);

    }
}