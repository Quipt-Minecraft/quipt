package com.quiptmc2.discord.plugins.events.custom;

import com.quiptmc2.discord.plugins.events.Event;

public abstract class CustomEvent<D extends EventData> extends Event<D> {
    public CustomEvent(D data) {
        super(data);
    }
}
