package com.quiptmc2.minecraft.location;

import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;

import java.math.BigDecimal;
import java.util.UUID;

public class Location extends ConfigObject implements Cloneable {

    UUID world;
    BigDecimal x;
    BigDecimal y;
    BigDecimal z;
    BigDecimal yaw;
    BigDecimal pitch;


    public Location(QuiptIntegration integration) {
        super(integration);
    }

    public BigDecimal x(){
        return x;
    }

    public BigDecimal y(){
        return y;
    }

    public BigDecimal z(){
        return z;
    }

    public BigDecimal yaw(){
        return yaw;
    }

    public BigDecimal pitch(){
        return pitch;
    }

    public UUID world(){
        return world;
    }

    public void world(UUID world){
        this.world = world;
    }

    public void x(double x){
        this.x = BigDecimal.valueOf(x);
    }

    public void y(double y){
        this.y = BigDecimal.valueOf(y);
    }

    public void z(double z){
        this.z = BigDecimal.valueOf(z);
    }

    public void yaw(float yaw){
        this.yaw = BigDecimal.valueOf(yaw);
    }

    public void pitch(float pitch){
        this.pitch = BigDecimal.valueOf(pitch);
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
