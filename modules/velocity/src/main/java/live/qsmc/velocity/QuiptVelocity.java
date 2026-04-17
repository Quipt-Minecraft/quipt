package live.qsmc.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import live.qsmc.velocity.commands.CommandExecutor;
import live.qsmc.velocity.commands.executors.UpdateCommand;

import java.nio.file.Path;


public class QuiptVelocity extends QuiptProxy {

    @Inject
    public QuiptVelocity(ProxyServer server, @DataDirectory Path dataDirectory) {
        super(server, dataDirectory);
    }



    @Override
    public void enable() {
        new CommandExecutor.Builder(new UpdateCommand(this)).register();
    }
}
