package com.quiptmc.core;

import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.config.ConfigMap;
import com.quiptmc.core.config.ConfigTemplate;
import com.quiptmc.core.config.TestConfig;
import com.quiptmc.core.objects.JsonTest;
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
}
