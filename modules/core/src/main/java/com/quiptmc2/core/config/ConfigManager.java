/*
 * Copyright (c) 2025. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.quiptmc2.core.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.toml.TomlFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.quiptmc2.core.QuiptIntegration;
import com.quiptmc2.core.config.objects.ConfigObject;
import com.quiptmc2.core.data.JsonSerializable;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Manages config files
 */
public class ConfigManager {

    private final QuiptIntegration integration;

    private final Map<String, ConfigObject.Factory<?>> factories = new HashMap<>();

    private final Map<String, Config> configs = new HashMap<>();

    private final Class<?>[] incompatibleTypes = {short.class, char.class, Short.class, Character.class, ArrayList.class};


    public ConfigManager(QuiptIntegration integration) {
        this.integration = integration;
    }

    public Map<String, ConfigObject.Factory<?>> factories() {
        return factories;
    }

    /**
     * Registers a factory for creating ConfigObject instances and associates it with its class name.
     *
     * @param factory The factory instance used to create ConfigObject instances.
     */
    public void factory(ConfigObject.Factory<?> factory) {
        integration.logger().log(integration.name() + "-QuiptConfig", "Registering factory for class: " + factory.getClassName());
        factories.put(factory.getClassName(), factory);
    }


    /**
     * Registers a config file for an integration.
     *
     * @param templateClass The class of the config file
     * @param <T>           The type of the config file
     * @return An instance of the config file registered under the integration
     */
    public <T extends Config> T register(Class<T> templateClass) {
        if (!templateClass.isAnnotationPresent(ConfigTemplate.class))
            throw new IllegalStateException("The ConfigTemplate class " + templateClass.getName() + " must have @ConfigFile annotation present, however none are detected.");
        ConfigTemplate templateData = templateClass.getAnnotation(ConfigTemplate.class);
        integration.logger().log("QuiptConfig", "Registering config file \"" + templateData.name() + "\" for integration: " + integration.name() + ".");
        File file;
        T content;
        try {
            file = file(integration, templateData);
            content = templateClass.getConstructor(File.class, String.class, ConfigTemplate.Extension.class, QuiptIntegration.class).newInstance(file, templateData.name(), templateData.ext(), integration);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |
                 InstantiationException e) {
            integration.logger().warn("Could not register config file: {} for {}.", templateClass.getName(), integration.name());
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("Could not create config file: " + templateClass.getName() + " for " + integration.name() + ".", e);
        }


        //Variables set. Now time to load the file or default values
        JSONObject writtenData = load(file, templateData.ext());

        write(content, writtenData);

        configs.put(templateData.name(), content);
        integration.logger().log(
                integration.name() + "-ConfigManager",
                content.write()
                        ? "Registered {} config file"
                        : "Failed to register {} config file",
                templateData.name()
        );
        return content;
    }

    private File file(QuiptIntegration integration, ConfigTemplate templateData) throws IOException {
        File file = new File(integration.folder(), templateData.name() + "." + templateData.ext().extension());
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (!file.exists()) {
            integration.logger().log("QuiptConfig", "Config file \"" + templateData.name() + "\" does not exist. Creating...");
            integration.logger().log("QuiptConfig", file.createNewFile() ? "Success" : "Failure");
        }
        return file;
    }

    private <T extends Config> void write(T content, JSONObject writtenData) {
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
//                    if
                    if (writtenValue instanceof JSONObject json) {
                        if (JsonSerializable.class.isAssignableFrom(configField.getType())) {
                            JsonSerializable serializable = (JsonSerializable) configField.getType().getDeclaredConstructor(com.quiptmc2.core.QuiptIntegration.class).newInstance(integration);
                            serializable.fromJson(json);
                            writtenValue = serializable;
                        }
                        if (ConfigObject.class.isAssignableFrom(configField.getType())) {
                            ConfigObject configObject = (ConfigObject) configField.getType().getDeclaredConstructor(com.quiptmc2.core.QuiptIntegration.class).newInstance(integration);
                            json.remove("integration");
                            configObject.fromJson(json);
                            writtenValue = configObject;
                        }
                    }
                    if (configField.getType() == BigDecimal.class) {
                        writtenValue = BigDecimal.valueOf(Double.parseDouble(writtenValue.toString()));
                    }
                    configField.set(content, writtenValue);
                }
            } catch (IllegalAccessException | InvocationTargetException | InstantiationException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Loads a configuration file and parses its content into a JSONObject.
     * <p>
     * The method supports multiple file formats as defined in {@link ConfigTemplate.Extension}.
     * It uses format-specific parsers to read the file and convert it into a JSONObject.
     * If the file is empty or contains invalid data, the method returns an empty JSONObject.
     *
     * @param file      The file to load.
     * @param extension The extension of the file, indicating the format to parse.
     * @return A JSONObject representing the parsed content of the file. If the file is empty
     * or an error occurs during parsing, a new empty JSONObject is returned.
     */
    public JSONObject load(File file, ConfigTemplate.Extension extension) {
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
     * Reloads the configuration associated with a specific integration and template class.
     * <p>
     * This method removes the existing configuration from the internal data store for the
     * specified integration and template, and re-registers it to ensure an updated configuration.
     *
     * @param template    The class of the configuration template to be reloaded.
     */
    public void reload(Class<? extends Config> template) {
        ConfigTemplate cf = template.getAnnotation(ConfigTemplate.class);
        configs.remove(cf.name());
        register(template);
    }

    /**
     * Gets a config file for a plugin
     *
     * @param clazz The class of the config file
     * @param <T>   The type of the config file
     * @return The config file
     */
    public <T extends Config> T config(Class<T> clazz) {
        return (T) configs.get(clazz.getAnnotation(ConfigTemplate.class).name());
    }

    /**
     * Gets a config file by name
     *
     * @param name The name of the config file
     * @return The config file
     */
    public Config config(String name) {
        return configs.get(name);
    }

    /**
     * Saves all config files
     */
    public void saveAll() {
        for (Config config : configs.values()) {
            save(config);
        }
    }

    /**
     * Saves a config file
     *
     * @param configContent The config file to save
     */
    public void save(Config configContent) {
        configContent.save();
    }

    /**
     * Resets the configuration manager by clearing all stored configuration data.
     * <p>
     * This method removes all entries from the internal data structure, effectively
     * resetting the manager to an initial state with no configurations.
     */
    public void reset() {
        configs.clear();
    }

    /**
     * Retrieves a list of all configuration names currently stored in the configuration manager.
     *
     * @return A list of strings representing the names of all stored configurations.
     */
    public List<String> all() {
        return new ArrayList<>(configs.keySet());
    }
}
