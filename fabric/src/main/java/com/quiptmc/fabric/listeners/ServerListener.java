package com.quiptmc.fabric.listeners;


import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.fabric.Initializer;
import com.quiptmc.fabric.api.FabricPlayer;
import com.quiptmc.fabric.api.FabricServer;
import com.quiptmc.minecraft.api.events.QuiptPlayerChatEvent;
import com.quiptmc.minecraft.api.events.QuiptPlayerJoinEvent;
import com.quiptmc.minecraft.api.events.QuiptPlayerLeaveEvent;
import com.quiptmc.fabric.api.FabricIntegration;
import com.quiptmc.minecraft.CoreUtils;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.json.JSONObject;

public class ServerListener implements ServerLifecycleEvents.ServerStopping, ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect, ServerMessageEvents.ChatMessage, ServerLifecycleEvents.ServerStarting, ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStopping(MinecraftServer server) {
    }

    @Override
    public void onServerStarted(MinecraftServer minecraftServer) {

    }

    @Override
    public void onServerStarting(MinecraftServer minecraftServer) {

    }

    @Override
    public void onChatMessage(SignedMessage signedMessage, ServerPlayerEntity serverPlayerEntity, MessageType.Parameters parameters) {

    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {

    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {

    }
//
//    @Override
//    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
//        System.out.println("Player " + handler.player.getGameProfile().getName() + " is ready to play");
//        QuiptPlayerJoinEvent event = new QuiptPlayerJoinEvent(FabricPlayer.of(handler.player), handler.player.getGameProfile().getName() + " joined the game");
//        ((Initializer.Quipt) CoreUtils.quipt()).events().handle(event);
//    }
//
//    @Override
//    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
//        System.out.println("Player " + handler.player.getGameProfile().getName() + " is disconnecting");
//        try {
//            QuiptPlayerLeaveEvent event = new QuiptPlayerLeaveEvent(FabricPlayer.of(handler.player), handler.player.getGameProfile().getName() + " left the game");
//            ((Initializer.Quipt) CoreUtils.quipt()).events().handle(event);
//        } catch (Exception e) {
//            System.out.println("There was an error.");
//            System.err.println(e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onServerStarting(MinecraftServer server) {
//        CoreUtils.quipt().api().initialize();
////        CoreUtils.integration().server(server);
//    }
//
//    @Override
//    public void onServerStarted(MinecraftServer server) {
//        FabricServer fabricServer = FabricServer.of(server);
//        JSONObject data = new JSONObject();
//        data.put("server_type", "fabric");
//        data.put("name", server.getName());
//        data.put("ip", server.getServerIp());
//        data.put("port", server.getServerPort());
//        data.put("max_players", server.getMaxPlayerCount());
//        data.put("online_players", server.getCurrentPlayerCount());
//        data.put("motd", server.getServerMotd());
//        data.put("version", server.getVersion());
//        JSONObject worlds = new JSONObject();
//        for(ServerWorld world : server.getWorlds()) {
//            JSONObject worldData = new JSONObject();
//            worldData.put("name", world.getRegistryKey().getValue().toString());
//            worldData.put("dimension", world.getRegistryKey().getValue().toString());
//            worldData.put("difficulty", world.getDifficulty().getId());
//            worldData.put("seed", world.getSeed());
//            worlds.put(world.getRegistryKey().getValue().toString(), worldData);
//        }
//        data.put("worlds", worlds);
//
//
//
//
//        QuiptIntegration.ApiManager.ApiResponse updateFeedback = (CoreUtils.quipt().api().api("server/update/" + CoreUtils.quipt().api().uuid(), data));
//        if(updateFeedback.result().equals(QuiptIntegration.ApiManager.ApiResponse.RequestResult.NO_ACTION)){
//            System.out.println("Server is not registered, registering...");
//            QuiptIntegration.ApiManager.ApiResponse registerFeedback = (CoreUtils.quipt().api().api("server/register/" + CoreUtils.quipt().api().uuid(), data));
//            if(registerFeedback.result().equals(QuiptIntegration.ApiManager.ApiResponse.RequestResult.SUCCESS)){
//                System.out.println("Server registered successfully.");
//            } else {
//                System.out.println("Failed to register server.");
//                System.out.println(registerFeedback.raw().toString(2));
//            }
//        } else {
//            System.out.println("Server updated successfully.");
//        }
//    }
//
//    @Override
//    public void onChatMessage(SignedMessage message, ServerPlayerEntity player, MessageType.Parameters parameters) {
//        QuiptPlayerChatEvent event = new QuiptPlayerChatEvent(FabricPlayer.of(player), message.getContent().getLiteralString());
//        ((Initializer.Quipt) CoreUtils.quipt()).events().handle(event);
//    }
}