package com.quiptmc.tests.core;

import com.quiptmc.core.data.Metadata;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.core.data.registries.RegistryKey;
import com.quiptmc.tests.core.messages.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatagenTests {


    @Test
    void testMetadata(){
        JSONObject data = Main.getNestedJson();
        Metadata metadata = Metadata.of(data);

        System.out.println(metadata.toString(2));

        assertEquals("Nest 1.1", metadata.get("nest1").get("nest1_1").value("name", String.class), "Nested name is not correct");
        assertEquals("Mustafa Miller", metadata.value("name", String.class), "Name is not correct");

    }



    @Test
    public void testRegistries() {
        Optional<RegistryKey> key = Registries.KEYS.register("messages");
        if (key.isEmpty()) throw new RuntimeException("Failed to register key");

        Registries.REGISTRAR.add(key.get(), new Registry<>(Message.class));
        Registry<Message> messages = Registries.REGISTRAR.get(key.get(), Message.class).orElseThrow(() -> new RuntimeException("Failed to get registry"));
        messages.register("test", new Message("Hello, World!"));

        if (messages.get("test").isPresent()) messages.get("test").get().send();

        assertTrue(messages.get("test").isPresent(), "Message is not present");
        assertEquals("Hello, World!", messages.get("test").get().message(), "Message is not correct");
    }
}
