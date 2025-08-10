package com.quiptmc.tests.core.factory;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.tests.core.config.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ObjectFactory {


    public static QuiptIntegration createIntegration(){
        return createIntegration("TestIntegration", "test_data");
    }

    public static QuiptIntegration createIntegration(String name, String folderPath){
        return new QuiptIntegration() {

            final Logger logger = LoggerFactory.getLogger(this.getClass());
            File file = new File(folderPath);


            @Override
            public void enable() {

                if (!file.exists())
                    log("TestIntegration", "Data folder does not exist. Creating... " + (file.mkdirs() ? "Success" : "Failed"));

                TestConfig config = ConfigManager.registerConfig(this, TestConfig.class);
                config.testConfig = ConfigManager.getNestedConfig(config, TestNestedConfig.class, "testConfig");
                config.save();
                log("TestIntegration", "Enabled");

            }

            @Override
            public void log(String tag, String message) {
                logger.info("[" + tag + "] " + message);
            }


            @Override
            public File dataFolder() {
                return file;
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            public String version() {
                return "";
            }

        };
    }
}
