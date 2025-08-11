package com.quiptmc.core.config;

import com.quiptmc.core.data.JsonSerializable;

public class ConfigObject implements JsonSerializable {

    public String id;
    public String className;

    public ConfigObject() {
        this.className = this.getClass().getName();
    }
}
