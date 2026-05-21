package live.qsmc.quipt.core;

import live.qsmc.quipt.core.events.*;
import live.qsmc.quipt.core.utils.TestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class IntegrationTests {

    public static Quipt QUIPT;
    public static QuiptIntegration TEST_INTEGRATION;

    @BeforeAll
    public static void setupQuipt() {
        QUIPT = Quipt.INSTANCE;
        QUIPT.enable();
        TEST_INTEGRATION = TestUtils.getTestIntegration();
        QUIPT.enable(TEST_INTEGRATION);
    }

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
