package com.quiptmc.paper.entity.entities;

import com.quiptmc.paper.entity.QuiptEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Dolphin;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class QuiptDolphin extends Dolphin implements QuiptEntity<Dolphin> {
    public QuiptDolphin(Location location) {
        super(EntityType.DOLPHIN, ((CraftWorld) location.getWorld()).getHandle());
        spawn(location, this);
    }
}