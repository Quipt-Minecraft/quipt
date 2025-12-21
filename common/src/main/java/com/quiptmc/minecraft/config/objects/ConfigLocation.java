package com.quiptmc.minecraft.config.objects;

import com.quiptmc.core.annotations.NotNull;
import com.quiptmc.core.config.ConfigObject;
import com.quiptmc.core.data.JsonSerializable;
import com.quiptmc.minecraft.utils.teleportation.Location;
import org.json.JSONObject;

public class ConfigLocation extends ConfigObject implements JsonSerializable {


    public double x, y, z;
    public float yaw, pitch;
    public String world;

    public ConfigLocation(String id, Location location) {
        super.id = id;
        this.x = location.x();
        this.y = location.y();
        this.z = location.z();
        this.yaw = location.yaw();
        this.pitch = location.pitch();
        this.world = location.world();
    }

    public ConfigLocation(JSONObject json) {
        fromJson(json);
    }

    public ConfigLocation(String id) {
        super.id = id;
    }

    public ConfigLocation(String id, int blockX, int blockY, int blockZ, float yaw, float pitch, @NotNull String world) {
        this(id);
        this.x = blockX;
        this.y = blockY;
        this.z = blockZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public ConfigLocation(String id, double x, double y, double z, float yaw, float pitch, @NotNull String world) {
        this(id);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

}
