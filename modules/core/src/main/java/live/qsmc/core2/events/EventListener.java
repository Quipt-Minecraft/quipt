package live.qsmc.core2.events;


public abstract class EventListener<T extends Event<?>> {

    Class<T> typeClass;

    public EventListener(Class<T> clazz) {
        typeClass = clazz;
    }

    public abstract void handle(T event);

    public void processs(Event<?> event) {
        if (typeClass.isInstance(event)) {
            handle(typeClass.cast(event));
        }
    }
}
