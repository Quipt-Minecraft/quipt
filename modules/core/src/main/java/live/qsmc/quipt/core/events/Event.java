package live.qsmc.quipt.core.events;


import live.qsmc.quipt.core.data.Wrapper;
import live.qsmc.quipt.core.data.annotations.Nullable;


public abstract class Event<D extends EventData> extends Wrapper<D> {

    EventProperties properties = new EventProperties();

    public Event(D original) {
        super(original);
    }

    public EventProperties properties() {
        return properties;
    }

    public interface Cancellable {

        boolean cancelled();
    }

}
