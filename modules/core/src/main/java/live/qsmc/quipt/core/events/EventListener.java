package live.qsmc.quipt.core.events;


public abstract class EventListener<E extends Event<D>, D extends EventData> {

    private final Class<E> eventClass;

    public EventListener(Class<E> clazz) {
        eventClass = clazz;
    }

    public Class<E> eventClass() {
        return eventClass;
    }

    public abstract void handle(E event);

    public EventProperties process(Event<?> event) {
        if (eventClass.isInstance(event)) {
            handle(eventClass.cast(event));
            return event.properties();
        }
        return null;
    }
}
