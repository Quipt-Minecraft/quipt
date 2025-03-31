package com.quiptmc.tests.minecraft;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.util.UUID;

public class MinecraftTests {

    @Test
    void testMinecraftServerPlayer() throws MalformedURLException {
        MinecraftServer server = new MinecraftServer.Builder("172.59.74.72", new JSONObject().put("secret", "You Should Change This To Literally Anything Else").put("callback_url", "http://localhost:5050/callback")).build();
        server.setupApi("http://localhost:8080/api");
        MinecraftServerPlayer player = server.apiManager().get().getPlayer(UUID.fromString("60191757-427b-421e-bee0-399465d7e852"));
        System.out.println(player.name());
        player.sendMessage("Hello, World!");
    }
}
