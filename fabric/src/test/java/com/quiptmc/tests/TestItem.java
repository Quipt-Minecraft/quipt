package com.quiptmc.tests;

public class TestItem {

    private final String name;

    public TestItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "TestItem{" +
                "name='" + name + '\'' +
                '}';
    }
}
