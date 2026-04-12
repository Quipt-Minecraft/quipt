package com.quiptmc.discord2.plugins.events.custom;

import com.quiptmc.discord2.plugins.events.Event;

public abstract class CustomEvent<D extends EventData> extends Event<D> {
    public CustomEvent(D data) {
        super(data);
    }
}
