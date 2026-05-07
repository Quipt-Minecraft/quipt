package live.qsmc.quipt.core.config.objects;

import live.qsmc.quipt.core.QuiptIntegration;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ConfigMap<T extends ConfigObject> extends ConfigObject {

    Map<String, T> configMap = new HashMap<>();

    public ConfigMap(QuiptIntegration integration) {
        super(integration);
    }

    public Collection<T> values() {
        return configMap.values();
    }

    public int size() {
        return configMap.size();
    }

    public boolean isEmpty() {
        return configMap.isEmpty();
    }

    /**
     * Retrieves the configuration object associated with the specified ID.
     *
     * @param id The ID of the configuration object to retrieve.
     * @return The configuration object associated with the given ID, or null if the ID is not found.
     */
    public T get(String id) {
        return configMap.getOrDefault(id, null);
    }

    public void remove(String id) {
        configMap.remove(id);
    }

    public void remove(T configObject) {
        configMap.remove(configObject.id);
    }

    public void put(T configObject) {
        configMap.put(configObject.id, configObject);
    }

    public boolean contains(String id) {
        return get(id) != null;
    }

    public boolean contains(T configObject) {
        return configMap.containsValue(configObject);
    }

    public boolean containsAll(T[] configObjects) {
        for (T obj : configObjects) {
            if (!contains(obj)) {
                return false;
            }
        }
        return true;
    }


    public void fromJson(JSONObject json) {
        for(String key : json.keySet()) {
            Object e = json.get(key);
            if (e instanceof JSONObject obj) {
                try {
                    String className = obj.getString("className");
                    ConfigObject.Factory<T> factory = (ConfigObject.Factory<T>) integration().configs().factories().get(className);

                    if (factory == null) {
                        integration().logger().error(integration().name() + "-ConfigMap", "No registered factory for class: " + className);
                        continue;
                    }

                    T t = factory.createFromJson(integration(), obj);
                    this.configMap.put(key, t);
                } catch (Exception ex) {
                    integration().logger().error(integration().name() + "-ConfigMap", "Error creating instance: " + ex.getMessage(), ex);
                }
            }
        }
    }

    @Override
    public JSONObject json() {
        JSONObject json = new JSONObject();
        for (String key : configMap.keySet()) {
            json.put(key, configMap.get(key).json());
        }
        return json;
    }


}
