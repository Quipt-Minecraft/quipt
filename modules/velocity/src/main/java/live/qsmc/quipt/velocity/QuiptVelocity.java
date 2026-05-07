package live.qsmc.quipt.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import live.qsmc.quipt.velocity.commands.CommandExecutor;
import live.qsmc.quipt.velocity.commands.executors.UpdateCommand;

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
