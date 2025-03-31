package com.quiptmc.minecraft.utils.loaders;

import com.quiptmc.core.annotations.Nullable;

public class ServerLoader<T> {

    Type type;
    T instance;

    public ServerLoader(Type type, @Nullable T instance) {
        this.type = type;
        this.instance = instance;
        load();
    }

    public void load() {
        switch (type) {
            case PAPER:
                System.out.println("Loading Paper server...");
                break;
            case FABRIC:
                System.out.println("Loading Fabric server...");
                break;
            default:
                System.out.println("Unknown server type");
        }
    }

    public Type type() {
        return type;
    }

    public T instance() {
        return instance;
    }


    public enum Type {
        PAPER, FABRIC
    }
}
