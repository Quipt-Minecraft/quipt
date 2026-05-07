package live.qsmc.quipt.core;

import live.qsmc.quipt.core.utils.TestUtils;

public class LauncherTests {

    public static void main(String[] args) {
        QuiptIntegration integration = TestUtils.getTestIntegration();
        Quipt.INSTANCE.webhooks().add("name", "id", "token");
        Quipt.INSTANCE.webhooks().save();
    }
}
