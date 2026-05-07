package live.qsmc.discord2;

import live.qsmc.core2.QuiptIntegration;
import live.qsmc.discord2.api.QDA;
import live.qsmc.discord2.config.BotConfig;
import live.qsmc.discord2.plugins.BotPluginLoader;
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
            plugins().enable();
            logger().log("Bot", "Bot started successfully.");
        } catch (InterruptedException e) {
            logger().log("Bot", "Error starting bot", e);
        }




    }

    public JDA jda(){return jda;}
    public QDA qda(){return qda;}
    public BotPluginLoader plugins(){return pluginLoader;}

}
