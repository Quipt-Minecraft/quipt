package com.quiptmc2.core;

import com.quiptmc2.core.utils.TestUtils;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

    @Test
    public void test() {
        QuiptIntegration testIntegration = TestUtils.getTestIntegration();
        testIntegration.webhooks().add("name", "id", "token");
        testIntegration.webhooks().save();

    }
}
