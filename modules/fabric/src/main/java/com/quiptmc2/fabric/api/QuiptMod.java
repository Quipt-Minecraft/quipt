package com.quiptmc2.fabric.api;

import com.quiptmc.core.data.Metadata;
import com.quiptmc2.minecraft.api.MinecraftIntegration;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.json.JSONObject;

import java.io.File;

public abstract class QuiptMod implements ModInitializer {

    private FabricIntegration quipt = null;

    public FabricIntegration quipt(){
        return quipt;
    }

    protected void run(ModContainer container){
        ModMetadata fabricMetadata = container.getMetadata();
        quipt = new FabricIntegration(metadata(fabricMetadata), container);
    }

    public void run(EntrypointContainer<QuiptMod> entrypoint) {
        ModMetadata metadata = entrypoint.getProvider().getMetadata();
        quipt = new FabricIntegration(metadata(metadata), entrypoint.getProvider());
        onInitialize();
    }

    private Metadata metadata(ModMetadata fabricMetadata){
        JSONObject data = new JSONObject();
        data.put("name", fabricMetadata.getName());
        data.put("version", fabricMetadata.getVersion().getFriendlyString());
        data.put("folder", new File("config/" + fabricMetadata.getId()));
        return Metadata.of(data);
    }

    public static class FabricIntegration extends MinecraftIntegration<ModContainer> {

        private String id;

        public FabricIntegration(Metadata metadata, ModContainer instance) {
            super(metadata, instance);
            id = instance.getMetadata().getId();
        }

        public String id() {
            return id;
        }

        @Override
        public void enable() {
            logger().log("Initialization", "Initializing " + id() + ".");
        }
    }
}
