package com.quiptmc.core.utils;

import com.quiptmc.core.QuiptIntegration;

import java.io.File;

public class TestUtils {
    public static QuiptIntegration getTestIntegration() {
        return new QuiptIntegration() {

            File file = new File("tests");

            @Override
            public void enable() {
                if(!file.exists()) {
                    log("TestIntegration", "Attempting to create data folder (" + file.getPath() + "): " + file.mkdirs());
                }
            }

            @Override
            public File dataFolder() {
                return file;
            }

            @Override
            public String name() {
                return "TestIntegration";
            }

            @Override
            public String version() {
                return "1.0.0";
            }

        };
    }
}
