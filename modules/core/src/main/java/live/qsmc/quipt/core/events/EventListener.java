package live.qsmc.quipt.core.events;


public abstract class EventListener<E extends Event<D>, D extends Event.Data, R> {

    private final Class<E> eventClass;

    public EventListener(Class<E> clazz) {
        eventClass = clazz;
    }

    public Class<E> eventClass() {
        return eventClass;
    }

    public abstract R handle(E event);

    public R process(Event<?> event) {
        if (eventClass.isInstance(event)) {
            return handle(eventClass.cast(event));
        }
        return null;
    }
}
