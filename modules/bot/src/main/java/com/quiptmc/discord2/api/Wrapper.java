package com.quiptmc.discord2.api;

public class Wrapper<T> {

    private final T data;

    public Wrapper(T data){
        this.data = data;
    }

    public T data(){
        return data;
    }
}
