package com.quiptmc.minecraft.utils.teleportation;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.core.config.files.TeleportationConfig;
import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.minecraft.utils.heartbeat.Flutter;
import com.quiptmc.minecraft.utils.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.teleportation.points.TeleportationPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LocationUtils {

    private static final List<TeleportRequest> requests = new ArrayList<>();
    private static final List<TeleportRequest> requestsAddQueue = new ArrayList<>();
    private static final List<TeleportRequest> requestsRemoveQueue = new ArrayList<>();
    static TeleportationConfig config;

    public static void start(MinecraftIntegration integration) {
        config = ConfigManager.registerConfig(CoreUtils.quipt(), TeleportationConfig.class);
        Flutter flutter = () -> {
            requests.addAll(requestsAddQueue);
            requestsAddQueue.clear();
            requests.removeAll(requestsRemoveQueue);
            requestsRemoveQueue.clear();
            requests.forEach(request -> {
                if (System.currentTimeMillis() - request.created() > 30000) {
                    request.deny();
                    requestsRemoveQueue.add(request);
                }
            });
            return true;
        };
        HeartbeatUtils.heartbeat(integration).addFlutter(flutter);
    }

    public static void put(TeleportationPoint point) {
        config.locations.put(point.name(), point.serialize());
    }

    public static TeleportationPoint get(String name) {
        return config.locations.has(name) ? new TeleportationPoint(config.locations.getJSONObject(name)) : null;
    }

    public static void remove(String name) {
        config.locations.remove(name);
    }

    public static Collection<TeleportationPoint> getAll() {
        List<TeleportationPoint> points = new ArrayList<>();
        for (String key : config.locations.keySet()) {
            points.add(new TeleportationPoint(config.locations.getJSONObject(key)));
        }
        return points;
    }

    public static Collection<TeleportationPoint> getAll(TeleportationPoint.Type type) {
        List<TeleportationPoint> points = new ArrayList<>();
        for (String key : config.locations.keySet()) {
            TeleportationPoint point = new TeleportationPoint(config.locations.getJSONObject(key));
            if (point.type() == type) points.add(point);
        }
        return points;
    }

    public static void save() {
        config.save();
    }

    public static TeleportRequest request(MinecraftPlayer requester, MinecraftPlayer target) {
        TeleportRequest request = new TeleportRequest(requester, target);
        request.send();
        requestsAddQueue.add(request);
        return request;
    }

    public static List<TeleportRequest> requests() {
        return requests;
    }

    public static class TeleportRequest {

        private final MinecraftPlayer requester;
        private final MinecraftPlayer target;
        private final long created;

        public TeleportRequest(MinecraftPlayer requester, MinecraftPlayer target) {
            this.requester = requester;
            this.target = target;
            this.created = System.currentTimeMillis();
        }

        public MinecraftPlayer requester() {
            return requester;
        }

        public MinecraftPlayer target() {
            return target;
        }

        public long created() {
            return created;
        }


        public void send() {
            requester.sendMessage(MessageUtils.get("quipt.tpr.sent.requester", target.getName()));
            target.sendMessage(MessageUtils.get("quipt.tpr.sent.target", requester.getName()));
        }

        public void accept() {
            requester.sendMessage(MessageUtils.get("quipt.tpr.accepted.requester", target.getName()));
            target.sendMessage(MessageUtils.get("quipt.tpr.accepted.target", requester.getName()));
            requester.teleport(target);
            LocationUtils.requestsRemoveQueue.add(this);
        }

        public void deny() {
            requester.sendMessage(MessageUtils.get("quipt.tpr.denied.requester", target.getName()));
            target.sendMessage(MessageUtils.get("quipt.tpr.denied.target", requester.getName()));
            LocationUtils.requestsRemoveQueue.add(this);
        }
    }
}
