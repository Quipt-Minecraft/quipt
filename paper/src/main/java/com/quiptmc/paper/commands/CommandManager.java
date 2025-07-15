package com.quiptmc.paper.commands;

import com.quiptmc.paper.api.PaperIntegration;
import com.quiptmc.paper.commands.executors.*;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager {
    public static void init(PaperIntegration integration) {

        new CommandExecutor.Builder(new ResourcePackCommand(integration)).setDescription("Command to manage Resource Pack").setAliases("rp", "resource", "pack").register();
        new CommandExecutor.Builder(new TeleportRequestCommand(integration)).setDescription("Request to teleport to a player").setAliases("tpa", "rtp", "tpr").register();
        new CommandExecutor.Builder(new TeleportRequestAcceptCommand(integration)).setDescription("Accept a teleport request").setAliases("tpaccept", "tpac", "atp").register();
        new CommandExecutor.Builder(new WarpCommand(integration)).setDescription("Warp command.").register();
        new CommandExecutor.Builder(new SudoCommand(integration)).setDescription("Sudo command").register();
    }


}
