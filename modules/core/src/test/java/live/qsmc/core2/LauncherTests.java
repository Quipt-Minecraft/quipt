package live.qsmc.core2;

import live.qsmc.core2.utils.TestUtils;

public class LauncherTests {

    public static void main(String[] args) {
        QuiptIntegration integration = TestUtils.getTestIntegration();
        Quipt.INSTANCE.webhooks().add("name", "id", "token");
        Quipt.INSTANCE.webhooks().save();
    }
}
