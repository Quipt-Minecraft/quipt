package live.qsmc.quipt.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import live.qsmc.quipt.minecraft.commands.executors.QuiptCommand;
import live.qsmc.quipt.minecraft.commands.executors.WebhookCommand;
import live.qsmc.quipt.velocity.commands.VelocityCommandExecutor;
import live.qsmc.quipt.velocity.commands.executors.VelocityCommonCommandExecutor;

import java.nio.file.Path;


public class QuiptVelocity extends QuiptProxy {

    @Inject
    public QuiptVelocity(ProxyServer server, @DataDirectory Path dataDirectory) {
        super(server, dataDirectory);
    }



    @Override
    public void enable() {
        new VelocityCommandExecutor.Builder(new VelocityCommonCommandExecutor<>(this, QuiptCommand.class, "vquipt")).register();
        new VelocityCommandExecutor.Builder(new VelocityCommonCommandExecutor<>(this, WebhookCommand.class, "vwebhook")).register();
    }
}
