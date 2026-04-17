package live.qsmc.core;

import live.qsmc.core.config.ConfigManager;
import live.qsmc.core.config.ConfigMap;
import live.qsmc.core.config.ConfigTemplate;
import live.qsmc.core.config.TestConfig;
import live.qsmc.core.objects.JsonTest;
import live.qsmc.core.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class ConfigTests {


    @Test
    public void testJsonObjects() {
        QuiptIntegration testIntegration = TestUtils.getTestIntegration();

//        JSONObject testData = new JSONObject();
//        testData.put("name", "QuiptMC");
//        testData.put("age", 5);
        JsonTest jsonTest = new JsonTest("test", "Blah", 4);

        TestConfig config = ConfigManager.registerConfig(testIntegration, TestConfig.class);
        System.out.println("Format: " + config.format(ConfigTemplate.Extension.JSON));
        ConfigMap<JsonTest> map = config.map;
//        config.map.put(jsonTest);
        config.save();
    }


    @Test
    public void testConfigClass(){
//        QuiptIntegration testIntegration = TestUtils.getTestIntegration();
//        ConfigManager.registerFactory(new TestFactory());
//
//        TestConfig config = ConfigManager.registerConfig(testIntegration, TestConfig.class);
//        System.out.println("Format: " + config.format(ConfigTemplate.Extension.QPT));
//        ConfigMap<JsonTest> map = config.map;
//
//        JsonTest jsonTest = map.get("test");
//        jsonTest.name = "Updated Name";
//        System.out.println("Format2: " + config.format(ConfigTemplate.Extension.QPT));
//
//        config.save();
    }
}
