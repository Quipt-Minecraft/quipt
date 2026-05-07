package live.qsmc.core2.events.custom;


import live.qsmc.core2.events.Event;

public abstract class CustomEvent<D extends EventData> extends Event<D> {
    public CustomEvent(D data) {
        super(data);
    }
}
