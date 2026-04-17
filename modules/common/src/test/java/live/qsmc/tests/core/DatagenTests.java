package live.qsmc.tests.core;

import live.qsmc.core.data.Metadata;
import live.qsmc.core.data.registries.Registries;
import live.qsmc.core.data.registries.Registry;
import live.qsmc.tests.core.messages.Message;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

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
        Registry<Message> messages = Registries.register("messages", ()->null);
        messages.register("test", new Message("Hello, World!"));

        if (messages.get("test").isPresent()) messages.get("test").get().send();

        assertTrue(messages.get("test").isPresent(), "Message is not present");
        assertEquals("Hello, World!", messages.get("test").get().message(), "Message is not correct");
    }
}
