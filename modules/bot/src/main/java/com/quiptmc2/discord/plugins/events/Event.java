package com.quiptmc2.discord.plugins.events;

import com.quiptmc2.discord.api.Wrapper;

public abstract class Event<T> extends Wrapper<T> {
    public Event(T original) {
        super(original);
    }
}
