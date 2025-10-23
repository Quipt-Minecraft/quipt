package com.quiptmc.core.config;

public record ConfigMapManager<T extends ConfigObject>(ConfigMap<T> map) {

    public T get(String id) {
        return map.get(id);
    }

    public boolean has(String id) {
        return map.contains(id);
    }

    public boolean has(T obj) {
        return map.contains(obj);
    }

    public void add(T obj) {
        if (!map.contains(obj)) {
            map.put(obj);
        }
    }
}
