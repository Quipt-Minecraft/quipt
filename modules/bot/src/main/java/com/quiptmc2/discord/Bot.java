package com.quiptmc2.discord;

import com.quiptmc.discord.api.QDA;
import com.quiptmc2.discord.api.QuiptListenerAdapter;
import com.quiptmc2.discord.plugins.BotPluginLoader;
import com.quiptmc.discord.logger.LoggerUtils;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.discord.config.BotConfig;
import com.quiptmc2.test.Test;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import java.io.File;

public class Bot extends QuiptIntegration {
    private static Bot instance = null;

    private JDA jda;
    private QDA qda;
    private BotPluginLoader pluginLoader;

    public static Bot instance(){
        if(instance == null){
            instance = new Bot();
        }
        return instance;
    }
    @Override
    public String name() {
        return "QuickBot";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public File folder() {
        return new File("bot-data");
    }

    @Override
    public void enable() {
        BotConfig config = configs().register(BotConfig.class);
        if(config.token.equals("<YOUR_BOT_TOKEN>")){
            logger().error("Initialization", "Bot token not set.", new ExceptionInInitializerError("Please set your bot token in the config file located at " + new File(folder(), "BotConfig.json").getAbsolutePath()));
            logger().log("Bot", "Please set your bot token in the config file located at: \"" + config.file().getAbsolutePath() + "\"");
            return;
        }
        try {
            JDA api = JDABuilder.createDefault(config.token, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
            api.awaitReady();
            this.jda = api;
            this.qda = new QDA(api);
            pluginLoader = new BotPluginLoader(this);
            pluginLoader.eventHandler().register("test-listener", new Test());
            jda.addEventListener(new QuiptListenerAdapter(this));
            logger().log("Bot", "Bot started successfully.");
        } catch (InterruptedException e) {
            logger().log("Bot", "Error starting bot", e);
        }




    }

    public JDA jda(){return jda;}
    public QDA qda(){return qda;}
    public BotPluginLoader plugins(){return pluginLoader;}
}
