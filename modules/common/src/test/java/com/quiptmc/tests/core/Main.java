package com.quiptmc.tests.core;

import org.json.JSONObject;

public class Main {

    public static void main(String[] args) {
        System.out.println(System.getenv("NEXUS_USERNAME"));
    }

    public static JSONObject getNestedJson() {
        JSONObject obj = new JSONObject();
        obj.put("name", "Mustafa Miller");
        obj.put("age", 18);
        JSONObject nest1 = new JSONObject();
        nest1.put("name", "Nest 1");
        nest1.put("age", 19);
        obj.put("nest1", nest1);
        JSONObject nest2 = new JSONObject();
        nest2.put("name", "Nest 2");
        nest2.put("age", 20);
        obj.put("nest2", nest2);
        JSONObject nest1_1 = new JSONObject();
        nest1_1.put("name", "Nest 1.1");
        nest1_1.put("age", 21);
        nest1.put("nest1_1", nest1_1);
        return obj;
    }
}
