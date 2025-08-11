package com.quiptmc.core.objects;


import com.quiptmc.core.config.ConfigObject;
import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

public class JsonTest extends ConfigObject {

    public String name;
    public int age;


    public JsonTest() {
    }

    public JsonTest(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

}
