package com.quiptmc2.discord.api;

public class Wrapper<T> {

    private final T original;

    public Wrapper(T original){
        this.original = original;
    }

    public T original(){
        return original;
    }
}
