package com.quiptmc.minecraft;


import com.quiptmc.minecraft.utils.MinecraftIntegration;
import com.quiptmc.minecraft.web.ResourcePackHandler;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

public class CoreUtils {

    private static MinecraftIntegration quipt = null;
    private static final Map<String, MinecraftIntegration> integrations = new HashMap<>();


    public static void preInit() {
        //DO NOT USE BUKKIT CODE HERE. ONLY REGISTER DATA UNRELATED TO PAPER AND QUIPT INTEGRATIONS
    }

    public static void init(MinecraftIntegration<?> integration) {
        if(CoreUtils.quipt == null) {
            CoreUtils.quipt = integration;
        }
        integrations.put(integration.name(), integration);
        integration.enable();
    }


    public static MinecraftIntegration quipt() {
        return quipt;
    }

    public static MinecraftIntegration integration(String name) {
        return integrations.get(name);
    }

    public static double getCPUUsage() {
        return ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getProcessCpuLoad() * 100;
    }

    public static ResourcePackHandler packHandler() {
        return quipt().packHandler();
    }
}
