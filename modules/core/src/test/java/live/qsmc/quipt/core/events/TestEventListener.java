package live.qsmc.quipt.core.events;

public class TestEventListener extends EventListener<TestEvent, TestEventData> {

    @Override
    public void handle(TestEvent event) {
        System.out.println("Test Event Triggered");
        event.properties().put("test_key", "This is a testObject");
    }
}
