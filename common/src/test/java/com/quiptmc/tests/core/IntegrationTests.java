package com.quiptmc.tests.core;

import com.quiptmc.core.QuiptIntegration;
import com.quiptmc.core.config.Config;
import com.quiptmc.core.config.ConfigManager;
import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.tests.core.config.*;
import com.quiptmc.tests.core.factory.ObjectFactory;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {

    Logger logger = LoggerFactory.getLogger("CoreTests");



    void destroyIntegration(QuiptIntegration integration, String test) {
        try {
            integration.destroy();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @Test
    void testConfigChangesPersist(){

        String valueToSet = "Test";
        QuiptIntegration integration = ObjectFactory.createIntegration();
        launchIntegration(integration, "testConfigChangesPersist");
        TestConfig config = ConfigManager.registerConfig(integration, TestConfig.class);
        config.testConfig = ConfigManager.getNestedConfig(config, TestNestedConfig.class, "testConfig");
        config.testConfig.nestedValue = valueToSet;
        config.save();

        ConfigManager.reset();


        QuiptIntegration integration2 = ObjectFactory.createIntegration();
        TestConfig config2 = ConfigManager.registerConfig(integration, TestConfig.class);
        config2.testConfig = ConfigManager.getNestedConfig(config2, TestNestedConfig.class, "testConfig");
        assertNotNull(config2.testConfig, "Config did not regenerate");
        assertEquals(valueToSet, config2.testConfig.nestedValue, "Config changes did not persist");

        destroyIntegration(integration, "testConfigChangesPersist");

        assertFalse(integration2.dataFolder().exists(), "Data folder was not deleted");
    }


    @Test
    void launchIntegrationTwiceClearWhenDone() throws InterruptedException {
        long started = System.currentTimeMillis();

        QuiptIntegration integration = ObjectFactory.createIntegration();
        launchIntegration(integration, "launchIntegrationTwiceClearWhenDone");
        TestConfig config = ConfigManager.getConfig(integration, TestConfig.class);
        config.testConfig.nestedValue = "Test";
        config.save();




        logger.info("launchIntegrationTwiceClearWhenDone: simulating shut-down");
        ConfigManager.reset();
        Registries.reset();

        logger.info("launchIntegrationTwiceClearWhenDone: creating second integration");
        QuiptIntegration integration2 = ObjectFactory.createIntegration();
        launchIntegration(integration2, "launchIntegrationTwiceClearWhenDone");

        destroyIntegration(integration2, "launchIntegrationTwiceClearWhenDone");

        integration2.log("TestIntegration", "Tests Complete! Time took: " + (System.currentTimeMillis() - started) + "ms");

        assertFalse(integration2.dataFolder().exists(), "Data folder was not deleted");
    }

    @Test
    void launchIntegrationClearWhenDone() {
        logger.info("Starting test: launchIntegrationClearWhenDone");
        long started = System.currentTimeMillis();

        QuiptIntegration integration = ObjectFactory.createIntegration();
        launchIntegration(integration, "launchIntegrationClearWhenDone");

        registerTestConfig(integration, TestConfig.class);
        registerTestConfig(integration, TestConfigJson.class);
        registerTestConfig(integration, TestConfigYaml.class);
        registerTestConfig(integration, TestConfigToml.class);



        destroyIntegration(integration, "launchIntegrationClearWhenDone");
        integration.log("TestIntegration", "Tests Complete! Time took: " + (System.currentTimeMillis() - started) + "ms");

        assertFalse(integration.dataFolder().exists());
    }

    private void registerTestConfig(QuiptIntegration integration, Class<? extends TestConfig> clazz) {
        TestConfig config = ConfigManager.registerConfig(integration, clazz);
        config.testConfig = ConfigManager.getNestedConfig(config, TestNestedConfig.class, "testConfig");
        config.save();
    }

//    @Test
//    void launchServer() throws IOException, InterruptedException {
//        logger.info("Starting test: launchServer");
//        File serverFolder = new File("server");
//        if(!serverFolder.exists()) serverFolder.mkdirs();
//        InputStream jar = NetworkUtils.downloadFile("https://api.papermc.io/v2/projects/paper/versions/1.21.4/builds/136/downloads/paper-1.21.4-136.jar");
//        NetworkUtils.saveStream(jar, new FileOutputStream(new File(serverFolder, "paper.jar")));
//        ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "paper.jar", "nogui");
//        processBuilder.directory(serverFolder);
//        Process serverProcess = processBuilder.start();
//
//        redirect(serverProcess.getInputStream(), System.out);
//
////        new Thread(() -> {
////            try (BufferedReader reader = new BufferedReader(new InputStreamReader(serverProcess.getInputStream()))) {
////                String line;
////                while ((line = reader.readLine()) != null) {
////                    System.out.println(line);
////                }
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }).start();
//
////        Thread.sleep(10000);
////
//        serverProcess.destroy();
//        serverFolder.delete();
//        assertTrue(true);
//    }

    void redirect(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) != -1) {
            output.write(buffer, 0, length);
        }
    }

    void launchIntegration(QuiptIntegration integration, String test){
        integration.enable();
        TestConfig config = ConfigManager.getConfig(integration, TestConfig.class);


        logger.info("{}: Verifying config files exist", test);
        checkConfigFiles(config);

        logger.info("{}: Verifying config contents", test);
        checkConfigContents(config);
    }

    void checkConfigContents(Config config) {
        assertFalse(config.json().isEmpty(), "Config is empty");
    }

    void checkConfigFiles(Config config) {
        assertTrue(config.file().exists(), "Config file does not exist");
    }

}
