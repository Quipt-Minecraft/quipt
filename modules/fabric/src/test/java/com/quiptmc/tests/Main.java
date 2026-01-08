package com.quiptmc.tests;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;

public class Main {

    public static void main(String[] args){
        Registry<String> reg1 = Registries.register("strings", String::new);
        reg1.register("test", "Hello, World!");

        Registry<TestItem> reg2 = Registries.register("test_items", ()->null);
        reg2.register("test_item", new TestItem("Test Item"));

        System.out.println(Registries.dump().toString(2));

    }
}
