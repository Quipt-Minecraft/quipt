package com.quiptmc.minecraft.utils.teleportation;

import com.quiptmc.minecraft.CoreUtils;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.minecraft.api.MinecraftPlayer;
import com.quiptmc.core.config.files.TeleportationConfig;
import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.utils.chat.MessageUtils;
import com.quiptmc.core.heartbeat.Flutter;
import com.quiptmc.core.heartbeat.HeartbeatUtils;
import com.quiptmc.minecraft.utils.loaders.ServerLoader;
import com.quiptmc.minecraft.utils.teleportation.points.TeleportationPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocationUtils {

    private static final List<TeleportRequest> requests = new ArrayList<>();
    private static final List<TeleportRequest> requestsAddQueue = new ArrayList<>();
    private static final List<TeleportRequest> requestsRemoveQueue = new ArrayList<>();
    static TeleportationConfig config;

    public static void start(MinecraftIntegration<? extends ServerLoader<?>> integration) {
        config = ConfigManager.registerConfig(CoreUtils.quipt(), TeleportationConfig.class);
        Flutter flutter = () -> {
            if(!requestsAddQueue.isEmpty()) {
                requests.addAll(requestsAddQueue);
                requestsAddQueue.clear();
            }
            if(!requestsRemoveQueue.isEmpty()) {
                requests.removeAll(requestsRemoveQueue);
                requestsRemoveQueue.clear();
            }

            requests.forEach(request -> {
                if (System.currentTimeMillis() - request.created() > TimeUnit.SECONDS.toMillis(config.request_timeout)) {
                    requestsRemoveQueue.add(request);
                    request.requester.sendMessage(MessageUtils.get("quipt.tpr.timeout.requester", MessageUtils.plainText(request.target().name())));
                    request.target.sendMessage(MessageUtils.get("quipt.tpr.timeout.target", MessageUtils.plainText(request.requester().name())));
                }
            });
            return true;
        };
        HeartbeatUtils.heartbeat(integration).flutter(flutter);
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
        System.out.println("Teleport request from " + requester.name() + " to " + target.name());
        TeleportRequest request = new TeleportRequest(requester, target);
        request.send();
        requestsAddQueue.add(request);
        return request;
    }

    public static List<TeleportRequest> requests() {
        return new ArrayList<>(requests);
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
            requester.sendMessage(MessageUtils.get("quipt.tpr.sent.requester", MessageUtils.plainText(target.name())));
            target.sendMessage(MessageUtils.get("quipt.tpr.sent.target", MessageUtils.plainText(requester.name()), config.request_timeout));
        }

        public void accept() {
            requester.sendMessage(MessageUtils.get("quipt.tpr.accepted.requester", MessageUtils.plainText(target.name())));
            target.sendMessage(MessageUtils.get("quipt.tpr.accepted.target", MessageUtils.plainText(requester.name())));
            requester.teleport(target);
            LocationUtils.requestsRemoveQueue.add(this);
        }

        public void deny() {
            requester.sendMessage(MessageUtils.get("quipt.tpr.denied.requester", MessageUtils.plainText(target.name())));
            target.sendMessage(MessageUtils.get("quipt.tpr.denied.target", MessageUtils.plainText(requester.name())));
            LocationUtils.requestsRemoveQueue.add(this);
        }
    }
}
