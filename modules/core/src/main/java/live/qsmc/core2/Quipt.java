package live.qsmc.core2;

import live.qsmc.core2.config.factories.GenericFactory;
import live.qsmc.core2.config.files.WebhookConfig;
import live.qsmc.core2.data.registries.Registries;
import live.qsmc.core2.data.registries.Registry;
import live.qsmc.core2.discord.Webhook;

import java.io.File;
import java.util.Optional;

public class Quipt extends QuiptIntegration {

    public static final Quipt INSTANCE = new Quipt();

    private final Registry<QuiptIntegration> integrationRegistry;
    /**
     * Registries instance for this integration
     */
    private Registries registries = null;

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

    public WebhookConfig webhooks(){
        if(configs().config(WebhookConfig.class) == null){
            logger().log("Webhooks", "Initializing Webhook Config...");
            configs().factory(new GenericFactory<>(Webhook.class));
            configs().register(WebhookConfig.class);
        }
        return configs().config(WebhookConfig.class);
    }


    public QuiptIntegration[] integrations() {
        return integrationRegistry.values();
    }
}
