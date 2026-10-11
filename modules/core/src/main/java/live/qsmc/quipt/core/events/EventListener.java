package live.qsmc.quipt.core.events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class EventListener<E extends Event<D>, D extends EventData> {

    private final Class<E> eventClass;

    @SuppressWarnings("unchecked")
    public EventListener() {
        Class<?> cls = getClass();
        while (cls.getSuperclass() != EventListener.class) {
            cls = cls.getSuperclass();
        }
        Type typeArg = ((ParameterizedType) cls.getGenericSuperclass()).getActualTypeArguments()[0];
        if (typeArg instanceof ParameterizedType pt) {
            eventClass = (Class<E>) pt.getRawType();
        } else {
            eventClass = (Class<E>) typeArg;
        }
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
