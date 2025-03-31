package com.quiptmc.tests.core;

import com.quiptmc.core.data.Metadata;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.data.registries.RegistryKey;
import com.quiptmc.tests.core.messages.Message;
import org.json.JSONObject;

import java.util.Optional;

public class Main {

    public static void main(String[] args) {


        Optional<RegistryKey> key = Registries.KEYS.register("messages");
        if (key.isEmpty()) throw new RuntimeException("Failed to register key");

        Registries.KEYS.register("messages").ifPresent(registryKey -> {
            Registries.REGISTRAR.add(registryKey, new Registry<>(Message.class));
            Registries.REGISTRAR.get(registryKey, Message.class).ifPresent(registry -> {
                registry.register("message1", new Message("Hello, World!"));
                registry.register("message2", new Message("Hello, World 2!"));
                registry.register("message3", new Message("Hello, World 3!"));
                registry.register("message4", new Message("Hello, World 4!"));
            });
        });

        Registries.KEYS.register("test").ifPresent(registryKey -> {
            Registries.REGISTRAR.add(registryKey, new Registry<>(Metadata.class));
            Registries.REGISTRAR.get(registryKey, Metadata.class).ifPresent(registry -> {
                registry.register("test", Metadata.of(getNestedJson()));
            });
        });

        Registries registries = Registries.REGISTRAR;

        System.out.println(registries.toString());

//        if (messages.get("test").isPresent()) messages.get("test").get().send();



//        TestConfigYaml yaml = ConfigManager.registerConfig(testIntegration, TestConfigYaml.class);
//        yaml.testConfig = ConfigManager.getNestedConfig(yaml, TestNestedConfig.class, "testConfig");
//        yaml.save();


    }

    public static JSONObject getNestedJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", "Mustafa Miller");
        obj.put("age", 18);
        JSONObject nest1 = new JSONObject();
        nest1.put("name", "Nest 1");
        nest1.put("age", 19);
        obj.put("nest1", nest1);
        JSONObject nest2 = new JSONObject();
        nest2.put("name", "Nest 2");
        nest2.put("age", 20);
        obj.put("nest2", nest2);
        JSONObject nest1_1 = new JSONObject();
        nest1_1.put("name", "Nest 1.1");
        nest1_1.put("age", 21);
        nest1.put("nest1_1", nest1_1);
        return obj;
    }
}
