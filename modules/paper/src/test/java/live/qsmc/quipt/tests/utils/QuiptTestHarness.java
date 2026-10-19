package live.qsmc.quipt.tests.utils;

import io.papermc.paper.plugin.configuration.PluginMeta;
import live.qsmc.quipt.paper.QuiptPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.UnsafeValues;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Logger;

import static org.mockito.Mockito.*;

public class QuiptTestHarness {

    private static Server mockServer;
    private static PluginManager mockPluginManager;

    public static Server getOrCreateServer() {
        if (mockServer == null) {
            mockServer = mock(Server.class, withSettings().lenient());
            mockPluginManager = mock(PluginManager.class, withSettings().lenient());
            UnsafeValues mockUnsafe = mock(UnsafeValues.class, withSettings().lenient());
            ItemFactory mockItemFactory = mock(ItemFactory.class, withSettings().lenient());
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class, withSettings().lenient());
            ServicesManager mockServicesManager = mock(ServicesManager.class, withSettings().lenient());

            when(mockServer.getPluginManager()).thenReturn(mockPluginManager);
            when(mockServer.getUnsafe()).thenReturn(mockUnsafe);
            when(mockServer.getItemFactory()).thenReturn(mockItemFactory);
            when(mockServer.getScheduler()).thenReturn(mockScheduler);
            when(mockServer.getServicesManager()).thenReturn(mockServicesManager);
            when(mockServer.getLogger()).thenReturn(Logger.getLogger("Minecraft"));
            when(mockServer.getName()).thenReturn("QuiptTestServer");
            when(mockServer.getVersion()).thenReturn("26.1.2");
            when(mockServer.getBukkitVersion()).thenReturn("26.1.2-R0.1-SNAPSHOT");

            try {
                Field serverField = Bukkit.class.getDeclaredField("server");
                serverField.setAccessible(true);
                serverField.set(null, mockServer);
            } catch (Exception ignored) {
            }
        }
        return mockServer;
    }

    @SuppressWarnings("unchecked")
    public static <T extends QuiptPlugin> T loadAndEnable(Class<T> pluginClass) {
        getOrCreateServer();
        try {
            Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
            T plugin = (T) unsafe.allocateInstance(pluginClass);

            File dataFolder = new File("build/tmp/test-plugin/" + pluginClass.getSimpleName());
            dataFolder.mkdirs();

            PluginDescriptionFile description = new PluginDescriptionFile(
                    pluginClass.getSimpleName(),
                    "1.0.0-TEST",
                    pluginClass.getName()
            );

            setField(plugin, "server", mockServer);
            setField(plugin, "dataFolder", dataFolder);
            setField(plugin, "logger", Logger.getLogger(pluginClass.getSimpleName()));
            setField(plugin, "description", description);
            setField(plugin, "pluginMeta", description);
            setField(plugin, "configFile", new File(dataFolder, "config.yml"));

            // Initialize and enable
            plugin.onEnable();
            setField(plugin, "isEnabled", true);

            when(mockPluginManager.getPlugin(pluginClass.getSimpleName())).thenReturn(plugin);
            when(mockPluginManager.isPluginEnabled(plugin)).thenReturn(true);

            return plugin;
        } catch (Throwable t) {
            t.printStackTrace();
            throw new RuntimeException("Failed to load and enable test plugin: " + pluginClass.getName(), t);
        }
    }

    private static void setField(Object target, String fieldName, Object value) {
        Class<?> clazz = target.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(target, value);
                return;
            } catch (NoSuchFieldException ignored) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                // ignore
                return;
            }
        }
    }
}
