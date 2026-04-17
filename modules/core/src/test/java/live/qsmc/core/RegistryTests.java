package live.qsmc.core;

import live.qsmc.core.data.registries.Registries;
import live.qsmc.core.data.registries.Registry;
import org.junit.jupiter.api.Test;

public class RegistryTests {

    @Test
    public void registry(){
        Registry<String> registry = Registries.register("test", () -> {return "default_value";});
    }
}
