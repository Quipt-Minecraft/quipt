package live.qsmc.quipt.core;

import live.qsmc.quipt.core.charity.CharityManager;
import live.qsmc.quipt.core.config.factories.GenericFactory;
import live.qsmc.quipt.core.config.files.QuiptConfig;
import live.qsmc.quipt.core.discord.WebhookManager;
import live.qsmc.quipt.core.data.registries.Registries;
import live.qsmc.quipt.core.data.registries.Registry;
import live.qsmc.quipt.core.discord.Webhook;
import live.qsmc.quipt.core.events.EventHandler;
import live.qsmc.quipt.core.server.QuiptServer;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public class Quipt extends QuiptIntegration {

    public static final Quipt INSTANCE = new Quipt();

    private final Registry<QuiptIntegration> integrationRegistry;
    /**
     * Registries instance for this integration
     */
    private Registries registries = null;

    private QuiptServer server = null;

    private EventHandler eventHandler = null;

    private CharityManager charities = null;

    public Quipt(){
        this.integrationRegistry = registries().register("integrations", () -> null);
        this.integrationRegistry.register("core", this);
    }

    public Registries registries() {
        if(registries == null){
            logger().log("Registries", "Initializing Registries...");
            registries = new Registries();
        }
        return registries;
    }

    public QuiptServer server() {
        if(server == null){
            logger().log("Server", "Initializing QuiptServer...");
            if(configs().config(QuiptConfig.class) == null){
                logger().log("Server", "Initializing Webhook Config...");
                configs().factory(new GenericFactory<>(QuiptConfig.WebData.class));
                configs().register(QuiptConfig.class);
            }
            QuiptConfig.WebData webData = configs().config(QuiptConfig.class).webData;
            QuiptServer.ServerConfig serverConfig = new QuiptServer.ServerConfig(QuiptServer.ServerProtocol.valueOf(webData.protocol.toUpperCase(Locale.ROOT)), webData.host, webData.port);
            server = new QuiptServer(this, serverConfig);
        }
        return server;
    }

    public Optional<QuiptIntegration> integration(String name) {
        return this.integrationRegistry.get(name);
    }

    @Override
    public String name() {
        return "Quipt";
    }

    @Override
    public String version() {
        return "0.0.1";
    }

    @Override
    public File folder() {
        return new File("quipt/data");
    }

    @Override
    public void enable() {
        logger().log("Initialization", "Quipt enabled");
    }

    public boolean enable(QuiptIntegration integration) {
        if(this.integrationRegistry.get(integration.name()).isPresent()){
            logger().log("Core", "Integration " + integration.name() + " is already enabled.");
            return false;
        }
        this.integrationRegistry.register(integration.name(), integration);
        integration.enable();
        logger().log("Core", "Integration " +  integration.name() + " enabled.");
        return true;
    }

    public WebhookManager webhooks(){
        if(configs().config(WebhookManager.class) == null){
            logger().log("Webhooks", "Initializing Webhook Config...");
            configs().factory(new GenericFactory<>(Webhook.class));
            configs().register(WebhookManager.class);
        }
        return configs().config(WebhookManager.class);
    }

    public EventHandler events(){
        if(eventHandler == null){
            logger().log("EventHandler", "Initializing EventHandler...");
            eventHandler = new EventHandler(this);
        }
        return eventHandler;
    }


    public Collection<QuiptIntegration> integrations() {
        return integrationRegistry.toMap().values();
    }

    public CharityManager charities() {
        if(charities == null){
            logger().log("Charities", "Initializing Charity Manager...");
            charities = new CharityManager();
        }
        return charities;
    }
}
