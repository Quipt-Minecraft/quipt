package com.quiptmc2.minecraft.location;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;

import java.util.UUID;

public class Location extends ConfigObject implements Cloneable {

    UUID world;
    double x;
    double y;
    double z;
    float yaw;
    float pitch;


    public Location(QuiptIntegration integration) {
        super(integration);
    }

    public double x(){
        return x;
    }

    public double y(){
        return y;
    }

    public double z(){
        return z;
    }

    public float yaw(){
        return yaw;
    }

    public float pitch(){
        return pitch;
    }

    public UUID world(){
        return world;
    }

    public void world(UUID world){
        this.world = world;
    }

    public void x(double x){
        this.x = x;
    }

    public void y(double y){
        this.y = y;
    }

    public void z(double z){
        this.z = z;
    }

    public void yaw(float yaw){
        this.yaw = yaw;
    }

    public void pitch(float pitch){
        this.pitch = pitch;
    }

    @Override
    public Location clone() {
        Location loc = new Location(integration());
        loc.world = world;
        loc.x = x;
        loc.y = y;
        loc.z = z;
        loc.yaw = yaw;
        loc.pitch = pitch;
        return loc;
    }
}
