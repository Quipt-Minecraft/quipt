/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.quiptmc.core.QuiptIntegration;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Manages config files
 */
public class ConfigManager {

    private static final Map<String, Config> data = new HashMap<>();

    private static final Class[] incompatibleTypes = {short.class, char.class, Short.class, Character.class, ArrayList.class};

    /**
     * Private constructor to prevent instantiation
     */
    private ConfigManager() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Registers a config file for a plugin
     *
     * @param integration The plugin to register the config file for
     * @param template    The class of the config file
     * @param <T>         The type of the config file
     * @return The config file
     */
    public static <T extends Config> T registerConfig(QuiptIntegration integration, Class<T> template) {
        try {


            if (template.isAnnotationPresent(ConfigTemplate.class)) {
                ConfigTemplate cf = template.getAnnotation(ConfigTemplate.class);
                if (NestedConfig.class.isAssignableFrom(template)) {
                    throw new IllegalArgumentException("NestedConfig is not supported");
                }

                integration.log("QuiptConfig", "Registering config file \"" + cf.name() + "\".");
                if (!integration.dataFolder().exists()) integration.dataFolder().mkdir();
                File file = new File(integration.dataFolder(), cf.name() + "." + cf.ext().extension());
                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                if (!file.exists()) {
                    integration.log("QuiptConfig", "Config file \"" + cf.name() + "\" does not exist. Creating...");
                    integration.log("QuiptConfig", file.createNewFile() ? "Success" : "Failure");
                }
                T content = template.getConstructor(File.class, String.class, ConfigTemplate.Extension.class, QuiptIntegration.class).newInstance(file, cf.name(), cf.ext(), integration);

                //Variables set. Now time to load the file or default values
                JSONObject writtenData = loadJson(file, cf.ext());

                assignFieldsFromJson(content, writtenData);

                data.put(integration.name() + "/" + cf.name(), content);
                content.save();
                return content;
            } else {
                throw new IllegalArgumentException("Class must have @ConfigFile annotation");
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends Config> void assignFieldsFromJson(T content, JSONObject writtenData) {
        for (Field configField : content.getContentValues()) {
            try {
                if (writtenData.has(configField.getName())) {
                    Object writtenValue = writtenData.get(configField.getName());
                    Arrays.stream(incompatibleTypes).filter(type -> type.isAssignableFrom(configField.getType())).forEach(type -> {
                        throw new IllegalArgumentException("Type " + type.getName() + " is not supported in config files");
                    });
                    if (configField.getType().isEnum()) {
                        writtenValue = Enum.valueOf((Class<Enum>) configField.getType(), (String) writtenValue);
                    }
                    if (writtenValue instanceof JSONObject json) {
                        if (NestedConfig.class.isAssignableFrom(configField.getType())) {
                            NestedConfig nestedConfig = (NestedConfig) configField.getType().getConstructor(Config.class, String.class, QuiptIntegration.class).newInstance(content, configField.getName(), content.integration());
                            assignFieldsFromJson(nestedConfig, json);
                            writtenValue = nestedConfig;
                        }
                    }
                    configField.set(content, writtenValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets a config file for a plugin
     *
     * @param integration The plugin to get the config file for
     * @param clazz       The class of the config file
     * @param <T>         The type of the config file
     * @return The config file
     */
    public static <T extends Config> T getConfig(QuiptIntegration integration, Class<T> clazz) {
        return (T) data.get(integration.name() + "/" + clazz.getAnnotation(ConfigTemplate.class).name());
    }

    /**
     * Gets a config file by name
     *
     * @param name The name of the config file
     * @return The config file
     */
    public static Config getConfig(String name) {
        return data.get(name);
    }

    public static <E extends Config, D extends NestedConfig<E>> D getNestedConfig(E parent, Class<D> nestedTemplate, String name) {
        JSONObject parentData = parent.json();


        JSONObject data;

        if (parentData.has(name)) {
            data = parentData.getJSONObject(name);
        } else {
            data = new JSONObject();
        }
        try {
            D nestedConfig = nestedTemplate.getConstructor(Config.class, String.class, QuiptIntegration.class).newInstance(parent, name, parent.integration());
            assignFieldsFromJson(nestedConfig, data);
            return nestedConfig;
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject loadJson(File file, ConfigTemplate.Extension extension) {
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder builder = new StringBuilder();
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\n");
            }
            String content = builder.toString();
            return switch (extension) {
                case QPT, JSON -> content.isEmpty() ? new JSONObject() : new JSONObject(content);
                case YAML -> {
                    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
                    JsonNode node = mapper.readTree(content);
                    ObjectMapper jsonMapper = new ObjectMapper();
                    String json = jsonMapper.writeValueAsString(node);
                    yield json.isEmpty() || json.equals("null") ? new JSONObject() : new JSONObject(json);
                }
                case XML -> {
                    ObjectMapper mapper = new ObjectMapper(new XmlFactory());
                    JsonNode node = mapper.readTree(content);
                    ObjectMapper jsonMapper = new ObjectMapper();
                    String json = jsonMapper.writeValueAsString(node);
                    yield json.isEmpty() || json.equals("null") ? new JSONObject() : new JSONObject(json);
                }
                case TOML -> {
                    ObjectMapper mapper = new ObjectMapper(new TomlFactory());
                    JsonNode node = mapper.readTree(content);
                    ObjectMapper jsonMapper = new ObjectMapper();
                    String json = jsonMapper.writeValueAsString(node);
                    yield json.isEmpty() || json.equals("null") ? new JSONObject() : new JSONObject(json);
                }
            };

        } catch (IOException e) {

            return new JSONObject();
        }
    }


    /**
     * Saves a config file
     *
     * @param configContent The config file to save
     */
    public static void saveConfig(Config configContent) {
        configContent.save();
    }


    /**
     * Saves all config files
     */
    public static void saveAll() {
        for (Config config : data.values()) {
            saveConfig(config);
        }
    }

    public static void reset() {
        data.clear();
    }
}
