package live.qsmc.core2.events;


import live.qsmc.core2.data.Wrapper;

public abstract class Event<T> extends Wrapper<T> {
    public Event(T original) {
        super(original);
    }
}
