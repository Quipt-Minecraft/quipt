package live.qsmc.discord2.plugins.events;

import live.qsmc.discord2.api.Wrapper;

public abstract class Event<T> extends Wrapper<T> {
    public Event(T original) {
        super(original);
    }
}
