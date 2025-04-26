package com.quiptmc.fabric.listeners;


import com.quiptmc.core.events.QuiptPlayerChatEvent;
import com.quiptmc.core.events.QuiptPlayerJoinEvent;
import com.quiptmc.core.events.QuiptPlayerLeaveEvent;
import com.quiptmc.fabric.api.FabricIntegration;
import com.quiptmc.fabric.utils.FabricConversionUtils;
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

public class ServerListener implements ServerLifecycleEvents.ServerStopping, ServerPlayConnectionEvents.Join, ServerPlayConnectionEvents.Disconnect, ServerMessageEvents.ChatMessage, ServerLifecycleEvents.ServerStarting, ServerLifecycleEvents.ServerStarted {
    @Override
    public void onServerStopping(MinecraftServer server) {
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        System.out.println("Player " + handler.player.getGameProfile().getName() + " is ready to play");
        QuiptPlayerJoinEvent event = new QuiptPlayerJoinEvent(FabricConversionUtils.player(handler.player), handler.player.getGameProfile().getName() + " joined the game");
        ((FabricIntegration) CoreUtils.quipt()).events().handle(event);
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler handler, MinecraftServer server) {
        QuiptPlayerLeaveEvent event = new QuiptPlayerLeaveEvent(FabricConversionUtils.player(handler.player), handler.player.getGameProfile().getName() + " left the game");
        ((FabricIntegration) CoreUtils.quipt()).events().handle(event);
    }

    @Override
    public void onServerStarting(MinecraftServer server) {
//        CoreUtils.integration().server(server);
    }

    @Override
    public void onServerStarted(MinecraftServer server) {
    }

    @Override
    public void onChatMessage(SignedMessage message, ServerPlayerEntity player, MessageType.Parameters parameters) {
        QuiptPlayerChatEvent event = new QuiptPlayerChatEvent(FabricConversionUtils.player(player), message.getContent().getLiteralString());
        ((FabricIntegration) CoreUtils.quipt()).events().handle(event);
    }
}