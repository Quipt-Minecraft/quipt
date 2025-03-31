package com.quiptmc.minecraft.utils.sessions;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.minecraft.api.MinecraftEntityType;
import com.quiptmc.minecraft.api.MinecraftMaterial;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.minecraft.api.MinecraftStatistic;
import com.quiptmc.minecraft.files.SessionConfig;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static final Map<UUID, JSONObject> CURRENT_SESSIONS = new HashMap<>();
    private static SessionConfig config;

    public static void start(QuiptIntegration plugin) {
        config = ConfigManager.registerConfig(plugin, SessionConfig.class);
    }

    public static JSONObject getSession(Audience player) {
        return CURRENT_SESSIONS.getOrDefault(player.get(Identity.UUID).orElse(null), null);
    }

    public static SessionConfig getConfig() {
        return config;
    }

    public static void finish() {
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            finishSession(player);
//        }
        config.save();
    }

    public static void startSession(MinecraftPlayer player) {
        UUID uid = player.get(Identity.UUID).orElseThrow();
        String name = MessageUtils.plainText(player.get(Identity.DISPLAY_NAME).orElseThrow());
        JSONObject session = new JSONObject();
        session.put("JOINED", System.currentTimeMillis());
        for (MinecraftStatistic stat : MinecraftStatistic.values()) {
            if (stat.getType().equals(MinecraftStatistic.Type.UNTYPED)) {
                session.put(stat.name() + "_start", player.getStatistic(stat));
                continue;
            }
            if (stat.getType().equals(MinecraftStatistic.Type.ENTITY)) {
                for (MinecraftEntityType type : MinecraftEntityType.values()) {
                    if (type.equals(MinecraftEntityType.UNKNOWN)) continue;
                    session.put(stat.name() + "_" + type.name() + "_start", player.getStatistic(stat, type));
                }
                continue;
            }
            if (stat.getType().equals(MinecraftStatistic.Type.BLOCK) || stat.getType().equals(MinecraftStatistic.Type.ITEM)) {
                for (MinecraftMaterial material : MinecraftMaterial.values()) {
//                    if (material.isLegacy()) continue;
                    session.put(stat.name() + "_" + material.name() + "_start", player.getStatistic(stat, material));
                }
            }
        }
        CURRENT_SESSIONS.put(uid, session);
        CoreUtils.integration().logger().log("Session", "Started session for " + name);
    }

    public static void finishSession(MinecraftPlayer player) {
        UUID uid = player.get(Identity.UUID).orElseThrow();
        String name = MessageUtils.plainText(player.get(Identity.DISPLAY_NAME).orElseThrow());
        if (!config.sessions.has(uid.toString()))
            config.sessions.put(uid.toString(), new JSONArray());
        JSONObject session = getSession(player);
        session.put("LEFT", System.currentTimeMillis());
        session.put("PLAYTIME", session.getLong("LEFT") - session.getLong("JOINED"));
        session.put("NAME", name);
        for (MinecraftStatistic stat : MinecraftStatistic.values()) {
            if (stat.getType() == MinecraftStatistic.Type.UNTYPED) {
                int previous = session.has(stat.name()) ? session.getInt(stat.name()) : 0;
                int recent = player.getStatistic(stat) - (session.has(stat.name() + "_start") ? session.getInt(stat.name() + "_start") : 0);
                int total = previous + recent;
                session.remove(stat.name() + "_start");
                if (total > 0) session.put(stat.name(), previous + recent);
            }
            if (stat.getType().equals(MinecraftStatistic.Type.ENTITY)) {
                for (MinecraftEntityType type : MinecraftEntityType.values()) {
                    if (type.equals(MinecraftEntityType.UNKNOWN)) continue;
                    int previous = session.has(stat.name() + "_" + type.name()) ? session.getInt(stat.name() + "_" + type.name()) : 0;
                    int recent = player.getStatistic(stat, type) - (session.has(stat.name() + "_" + type.name() + "_start") ? session.getInt(stat.name() + "_" + type.name() + "_start") : 0);
                    int total = previous + recent;
                    if (total > 0) {
                        if (!session.has(stat.name())) session.put(stat.name(), new JSONObject());
                        session.getJSONObject(stat.name()).put(type.name(), total);
                    }
                    session.remove(stat.name() + "_" + type.name() + "_start");
                }
            }
            if (stat.getType().equals(MinecraftStatistic.Type.BLOCK) || stat.getType().equals(MinecraftStatistic.Type.ITEM)) {
                for (MinecraftMaterial material : MinecraftMaterial.values()) {
//                    if (material.isLegacy()) continue;
                    int previous = session.has(stat.name() + "_" + material.name()) ? session.getInt(stat.name() + "_" + material.name()) : 0;
                    int recent = player.getStatistic(stat, material) - (session.has(stat.name() + "_" + material.name() + "_start") ? session.getInt(stat.name() + "_" + material.name() + "_start") : 0);
                    int total = previous + recent;
                    if (total != 0) {
                        if (!session.has(stat.name())) session.put(stat.name(), new JSONObject());
                        session.getJSONObject(stat.name()).put(material.name(), total);
                    }
                    session.remove(stat.name() + "_" + material.name() + "_start");
                }
            }
        }
        config.sessions.getJSONArray(uid.toString()).put(session);
        CURRENT_SESSIONS.remove(uid);
        config.save();
        CoreUtils.integration().logger().log("Session", "Finished session for " + name);
    }

    public static long getOverallPlaytime(Audience player) {
        long playtime = 0;
        JSONArray sessions = getSessions(player);
        for (int i = 0; i != sessions.length(); i++) {
            JSONObject session = sessions.getJSONObject(i);
            if (session.has("PLAYTIME")) playtime = playtime + session.getLong("PLAYTIME");
            else {
                playtime = playtime + (System.currentTimeMillis() - getSession(player).getLong("JOINED"));
            }
        }
        return playtime;
    }

    public static JSONArray getSessions(Audience player) {
        UUID uid = player.get(Identity.UUID).orElseThrow();
        String name = MessageUtils.plainText(player.get(Identity.DISPLAY_NAME).orElseThrow());
        final JSONArray sessions = new JSONArray();
        if (config.sessions.has(uid.toString())) {
            JSONArray old_ses = config.sessions.getJSONArray(uid.toString());
            for (int i = 0; i != old_ses.length(); i++)
                sessions.put(old_ses.getJSONObject(i));
        }
        sessions.put(getSession(player));
        return sessions;

    }
}

