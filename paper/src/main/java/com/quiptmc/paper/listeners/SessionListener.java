package com.quiptmc.paper.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SessionListener implements Listener {

    public SessionListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
//        SessionManager.startSession(e.getPlayer());
    }

//    @EventHandler
//    public void onPlayerLeave(PlayerQuitEvent e){
//        SessionManager.finishSession(e.getPlayer());
//    }
}