package com.quiptmc.core;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.ConfigMap;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.TestConfig;
import com.quiptmc.core.objects.JsonTest;
import com.quiptmc.core.objects.factories.TestFactory;
import com.quiptmc.core.utils.TestUtils;
import org.json.JSONObject;
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
        QuiptIntegration testIntegration = TestUtils.getTestIntegration();
        ConfigManager.registerFactory(new TestFactory());

        TestConfig config = ConfigManager.registerConfig(testIntegration, TestConfig.class);
        System.out.println("Format: " + config.format(ConfigTemplate.Extension.QPT));
        ConfigMap<JsonTest> map = config.map;

        JsonTest jsonTest = map.get("test");
        jsonTest.name = "Updated Name";
        System.out.println("Format2: " + config.format(ConfigTemplate.Extension.QPT));

        config.save();
    }
}
