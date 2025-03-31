package com.quiptmc.paper.entity.entities;

import com.quiptmc.paper.entity.QuiptEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class QuiptBee extends Bee implements QuiptEntity<Bee> {
    public QuiptBee(Location location) {
        super(EntityType.BEE, ((CraftWorld) location.getWorld()).getHandle());
        spawn(location,this);

    }
}