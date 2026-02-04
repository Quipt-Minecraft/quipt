package com.quiptmc2.discord.plugins;

import com.quiptmc2.core.QuiptIntegration;

public abstract class BotPlugin {

    private String name = null;
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private BotPluginLoader pluginLoader;

    public BotPlugin(BotPluginLoader pluginLoader){
        this.pluginLoader = pluginLoader;
    }


    public abstract void enable();

    public abstract void disable();

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public QuiptIntegration.Logger logger(){
        return pluginLoader.bot().logger();
    }

    public ClassLoader loader(){
        return classLoader;
    }
}