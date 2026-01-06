package com.quiptmc2.core;

import com.quiptmc2.core.utils.TestUtils;

public class LauncherTests {

    public static void main(String[] args) {
        QuiptIntegration integration = TestUtils.getTestIntegration();
        integration.webhooks().add("name", "id", "token");
        integration.webhooks().save();
    }
}
