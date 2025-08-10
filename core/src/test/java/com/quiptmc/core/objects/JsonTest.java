package com.quiptmc.core.objects;


import com.quiptmc.core.data.JsonSerializable;
import org.json.JSONObject;

public class JsonTest implements JsonSerializable {

    public String name;
    public int age;

    public JsonTest() {
    }

    public JsonTest(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
