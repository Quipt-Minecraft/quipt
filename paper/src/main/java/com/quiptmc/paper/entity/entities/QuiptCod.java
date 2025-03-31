package com.quiptmc.paper.entity.entities;

import com.quiptmc.paper.entity.QuiptEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class QuiptCod extends Cod implements QuiptEntity<Cod> {
    public QuiptCod(Location location) {
        super(EntityType.COD, ((CraftWorld) location.getWorld()).getHandle());
        spawn(location,this);

    }
}