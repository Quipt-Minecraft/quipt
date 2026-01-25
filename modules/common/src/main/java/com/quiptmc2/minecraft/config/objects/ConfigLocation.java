package com.quiptmc2.minecraft.config.objects;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;
import com.quiptmc2.core.data.annotations.NotNull;
import com.quiptmc2.minecraft.location.Location;
import org.json.JSONObject;

public class ConfigLocation extends ConfigObject {


    public double x, y, z;
    public float yaw, pitch;
    public String world;

    public ConfigLocation(QuiptIntegration integration, String id, Location location) {
        super(integration);
        super.id = id;
        this.x = location.x();
        this.y = location.y();
        this.z = location.z();
        this.yaw = location.yaw();
        this.pitch = location.pitch();
        this.world = location.world().toString();
    }

    public ConfigLocation(QuiptIntegration integration, JSONObject json) {
        super(integration);
        fromJson(json);
    }

    public ConfigLocation(QuiptIntegration integration, String id) {
        super(integration);
        super.id = id;
    }

    public ConfigLocation(QuiptIntegration integration, String id, int blockX, int blockY, int blockZ, float yaw, float pitch, @NotNull String world) {
        this(integration, id);
        this.x = blockX;
        this.y = blockY;
        this.z = blockZ;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public ConfigLocation(QuiptIntegration integration, String id, double x, double y, double z, float yaw, float pitch, @NotNull String world) {
        this(integration, id);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

}
