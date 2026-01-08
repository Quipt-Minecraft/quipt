package com.quiptmc.minecraft.api.statistics;

public class MinecraftStat {


    public Type type() {
        return Type.UNTYPED;
    }

    public String name() {
        return "";
    }

    public enum Type {
        UNTYPED, ENTITY, BLOCK, ITEM;
    }
}
