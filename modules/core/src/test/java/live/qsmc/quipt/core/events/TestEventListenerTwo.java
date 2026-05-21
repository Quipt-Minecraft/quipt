package live.qsmc.quipt.core.events;

public class TestEventListenerTwo extends EventListener<TestEventTwo, TestEventDataTwo>{
    public TestEventListenerTwo() {
        super(TestEventTwo.class);
    }

    @Override
    public void handle(TestEventTwo event) {
        System.out.println("Test Event Two Triggered");
        event.properties().put("test_key_2", "This is a testObject");
    }
}
