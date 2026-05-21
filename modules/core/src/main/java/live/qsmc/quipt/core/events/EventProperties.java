package live.qsmc.quipt.core.events;

import live.qsmc.quipt.core.data.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EventProperties {

    private final Map<String, Object> additionalData = new HashMap<>();



    /**
     * Store additional data in this event for other listeners or handlers to access.
     * @param key the key to store the data under
     * @param value the data value
     */
    public void put(String key, Object value) {
        additionalData.put(key, value);
    }

    /**
     * Retrieve additional data from this event.
     * @param key the key of the data to retrieve
     * @return the data value, or null if not found
     */
    public @Nullable Object get(String key) {
        return additionalData.get(key);
    }

    /**
     * Retrieve additional data from this event with type safety.
     * @param key the key of the data to retrieve
     * @param type the expected type of the data
     * @return the data value cast to the specified type, or null if not found or type mismatch
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = additionalData.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Check if additional data exists for the given key.
     * @param key the key to check
     * @return true if data exists for the key, false otherwise
     */
    public boolean has(String key) {
        return get(key) != null;
    }
}
