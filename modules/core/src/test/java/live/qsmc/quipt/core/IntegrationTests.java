package live.qsmc.quipt.core;

import live.qsmc.quipt.core.charity.el.ExtraLifePlatform;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.ConfigTemplate;
import live.qsmc.quipt.core.events.*;
import live.qsmc.quipt.core.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTests {

    public static Quipt QUIPT;
    public static QuiptIntegration TEST_INTEGRATION;

    @BeforeAll
    public static void setupQuipt() throws InterruptedException {
        QUIPT = Quipt.INSTANCE;
        QUIPT.enable();
        TEST_INTEGRATION = TestUtils.getTestIntegration();
        QUIPT.enable(TEST_INTEGRATION);
    }

    @Test
    public void testFakeConfig() throws IOException {
        String name = System.currentTimeMillis() + "";
        ConfigTemplate.Extension ext = ConfigTemplate.Extension.JSON;
        File file = TEST_INTEGRATION.configs().file(name, ext);
        Config config = new Config(file, name, ext, TEST_INTEGRATION) {
            @Override
            public File file() {
                return super.file();
            }

            @Override
            public void update(Field versionField, int storedVersion) {

            }
        };
        TEST_INTEGRATION.configs().register(config);
    }

//    @Test
//    public void testLastLife() throws InterruptedException {
//        ExtraLifePlatform charity = QUIPT.charities().initialize(TEST_INTEGRATION, "extralife", ExtraLifePlatform.class);
//        Thread.sleep(5000L);
//        charity.config().save();
//        assertFalse(charity.config().processed.isEmpty());
//    }

    @Test
    public void testEvents(){
        QUIPT.events().register(new TestEventListener());
        QUIPT.events().register(new TestEventListenerTwo());
        EventHandleResult results = QUIPT.events().handle(new TestEvent(new TestEventData()));
        System.out.println("Event 1");
        EventHandleResult results2 = QUIPT.events().handle(new TestEventTwo(new TestEventDataTwo()));
        System.out.println("Event 1");

        results.results();
    }

    @Test
    public void test(){
        Quipt.INSTANCE.enable();
        System.out.println("test");
    }
    @Test
    public void testWebhooks() {
//        QuiptIntegration testIntegration = TestUtils.getTestIntegration();
//        testIntegration.webhooks().add("name", "id", "token");
//        testIntegration.webhooks().save();

    }
}
