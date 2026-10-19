package live.qsmc.quipt.tests;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.paper.api.players.PaperPlayer;
import live.qsmc.quipt.paper.api.players.PaperPlayers;
import live.qsmc.quipt.tests.utils.QuiptPluginTest;
import live.qsmc.quipt.tests.utils.QuiptTestHarness;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaperMockServerTest {

    private Server server;
    private QuiptPluginTest plugin;

    @BeforeEach
    public void setUp() {
        server = QuiptTestHarness.getOrCreateServer();
        plugin = QuiptTestHarness.loadAndEnable(QuiptPluginTest.class);
    }

    @AfterEach
    public void tearDown() {
        if (plugin != null && plugin.isEnabled()) {
            plugin.onDisable();
        }
    }

    @Test
    public void testServerAndPluginInitialization() {
        assertNotNull(server, "Server should be initialized");
        assertNotNull(plugin, "QuiptPluginTest should be loaded and enabled");
        assertTrue(plugin.isEnabled(), "Plugin should be enabled through server lifecycle");
        assertNotNull(plugin.integration(), "Integration should be registered on enable");
        assertTrue(Quipt.INSTANCE.integration(plugin.integration().name()).isPresent(), "Quipt core should have registered the paper integration");

        plugin.integration().players().save();
    }

    @Test
    public void testPlayerLifecycleInServerEnvironment() {
        Player player = mock(Player.class, withSettings().lenient());
        UUID uuid = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(uuid);
        when(player.getName()).thenReturn("TestUser");

        PaperPlayers paperPlayers = plugin.integration().players();
        PaperPlayer paperPlayer = paperPlayers.of(player);
        assertNotNull(paperPlayer);
        assertEquals(uuid, paperPlayer.uuid());
    }
}
