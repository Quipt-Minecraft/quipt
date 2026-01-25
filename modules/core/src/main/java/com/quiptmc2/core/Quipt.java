package com.quiptmc2.core;

import com.quiptmc2.core.config.factories.GenericFactory;
import com.quiptmc2.core.config.files.WebhookConfig;
import com.quiptmc2.core.data.registries.Registry;
import com.quiptmc2.core.discord.Webhook;

import java.io.File;
import java.util.Optional;

public class Quipt extends QuiptIntegration {

    public static final Quipt INSTANCE = new Quipt();

    private final Registry<QuiptIntegration> integrationRegistry;

    public Quipt(){
        this.integrationRegistry = registries().register("integrations", () -> null);
        this.integrationRegistry.register("core", this);
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
        System.out.println("Quipt enabled");
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


}
