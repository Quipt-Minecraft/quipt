package com.quiptmc2.paper;

import com.quiptmc2.minecraft.config.files.ResourceConfig;

public class Quipt extends QuiptPlugin {


    @Override
    public void enable() {
        if(integration.configs().config(ResourceConfig.class) == null)
            integration.configs().register(ResourceConfig.class);
        ResourceConfig config = integration.configs().config(ResourceConfig.class);
        if(config.enabled){
            integration.packHandler().start();
        }
        integration.logger().log("Quipt", "Quipt Plugin for Paper enabled!");
    }
}
