package live.qsmc.discord2.plugins.events.custom;

import live.qsmc.discord2.plugins.events.Event;

public abstract class CustomEvent<D extends EventData> extends Event<D> {
    public CustomEvent(D data) {
        super(data);
    }
}
