package com.quiptmc.minecraft.api;

public class MinecraftStatistic {
    public static MinecraftStatistic[] values() {
        return null;
    }

    public Type getType() {
        return Type.UNTYPED;
    }

    public String name() {
        return "";
    }

    public enum Type {
        UNTYPED, ENTITY, BLOCK, ITEM;
    }
}
