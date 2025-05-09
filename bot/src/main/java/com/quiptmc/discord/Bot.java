package com.quiptmc.discord;

import com.quiptmc.discord.api.QDA;
import com.quiptmc.discord.api.plugins.BotPluginLoader;
import com.quiptmc.discord.logger.LoggerUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.json.JSONObject;

import java.io.*;

public class Bot {

    private static JDA jda;
    private static QDA qda;
    private static JSONObject config;
    private static BotPluginLoader pluginLoader;

    public static void main(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File config = new File("config.json");
            if (!config.exists()) if (config.createNewFile()) {
                LoggerUtils.error("Config file generated.", "=");
                //write to file
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("bot_token", "<bot_token>");


                //write to file
                try (FileWriter writer = new FileWriter(config)) {
                    writer.write(jsonObject.toString(2));
                } catch (IOException e) {
                }

            }
            BufferedReader reader = new BufferedReader(new FileReader("config.json"));

            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();


        } catch (IOException ex) {
            LoggerUtils.error("Config", "Config File couldn't be generated or accessed. Please check console for more details.", ex);
        }
        String config = stringBuilder.toString();
        start(config.isEmpty() ? new JSONObject() : new JSONObject(config));
    }

    public static void start(JSONObject config) {
        Bot.config = config;
        try {
            JDA api = JDABuilder.createDefault(config.getString("bot_token"), GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS).setMemberCachePolicy(MemberCachePolicy.ALL).build();
            api.awaitReady();
            Bot.jda = api;
            Bot.qda = new QDA(api);
            pluginLoader = new BotPluginLoader();
            LoggerUtils.log("Bot", "Bot started successfully.");
        } catch (InterruptedException e) {
            LoggerUtils.log("Bot", "Error starting bot", e);
        }
    }

    public static JSONObject config(){
        return config;
    }

    public static QDA qda(){
        return qda;
    }

    public static JDA jda() {
        return jda;
    }

    public static BotPluginLoader pluginLoader(){
        return pluginLoader;
    }
}