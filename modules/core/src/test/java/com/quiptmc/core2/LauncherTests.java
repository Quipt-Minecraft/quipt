package com.quiptmc.core2;

import com.quiptmc.core2.utils.TestUtils;

public class LauncherTests {

    public static void main(String[] args) {
        QuiptIntegration integration = TestUtils.getTestIntegration();
        Quipt.INSTANCE.webhooks().add("name", "id", "token");
        Quipt.INSTANCE.webhooks().save();
    }
}
