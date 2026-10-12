package live.qsmc.quipt.discord.plugins;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.discord.BotModule;

public abstract class BotPlugin extends QuiptIntegration {

    private String name = null;
    private final ClassLoader classLoader = this.getClass().getClassLoader();
    private final BotPluginLoader pluginLoader;


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

    public BotModule bot(){
        return pluginLoader.bot();
    }

    public ClassLoader loader(){
        return classLoader;
    }
}