package com.quiptmc.discord2.plugins;

import com.quiptmc.core2.QuiptIntegration;
import com.quiptmc.discord2.Bot;

public abstract class BotPlugin extends QuiptIntegration {

    private String name = null;
    private ClassLoader classLoader = this.getClass().getClassLoader();
    private BotPluginLoader pluginLoader;


    public BotPlugin(BotPluginLoader pluginLoader){
        this.pluginLoader = pluginLoader;
    }


    public abstract void disable();

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public Bot bot(){
        return pluginLoader.bot();
    }

    public ClassLoader loader(){
        return classLoader;
    }
}