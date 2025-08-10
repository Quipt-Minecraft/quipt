package com.quiptmc.core;

import com.quiptmc.core.config.ConfigManager;
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
//        JsonTest jsonTest = new JsonTest(testData);

        TestConfig config = ConfigManager.registerConfig(testIntegration, TestConfig.class);
        config.save();
        System.out.println(config.jsonTest.json().toString(2));
    }
}
