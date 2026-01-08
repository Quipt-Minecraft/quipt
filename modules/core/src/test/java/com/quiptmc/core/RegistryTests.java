package com.quiptmc.core;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import org.junit.jupiter.api.Test;

public class RegistryTests {

    @Test
    public void registry(){
        Registry<String> registry = Registries.register("test", () -> {return "default_value";});
    }
}
