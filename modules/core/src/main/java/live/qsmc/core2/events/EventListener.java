package live.qsmc.core2.events;


public abstract class EventListener<E extends Event<D>, D extends EventData<R>, R> {

    private final Class<E> eventClass;

    public EventListener(Class<E> clazz) {
        eventClass = clazz;
    }

    public Class<E> eventClass() {
        return eventClass;
    }

    public abstract R handle(E event);

    public R processs(Event<?> event) {
        if (eventClass.isInstance(event)) {
            return handle(eventClass.cast(event));
        }
        return null;
    }
}
