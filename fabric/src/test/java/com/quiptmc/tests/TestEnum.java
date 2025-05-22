package com.quiptmc.tests;

import net.minecraft.util.StringIdentifiable;

public enum TestEnum implements StringIdentifiable {
    TEST1,
    TEST2,
    TEST3;

    @Override
    public String asString() {
        return name();
    }
}