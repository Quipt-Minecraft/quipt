package com.quiptmc.discord2.plugins.events;

import com.quiptmc.discord2.api.Wrapper;

public abstract class Event<T> extends Wrapper<T> {
    public Event(T original) {
        super(original);
    }
}
