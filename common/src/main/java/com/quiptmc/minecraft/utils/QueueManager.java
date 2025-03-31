package com.quiptmc.minecraft.utils;

public class QueueManager {
    public static QueueTask queueTask(String init, QueueTask task) {
        return task;
    }

    @FunctionalInterface
    public interface QueueSomething<T> {

        T progress();

    }
}
