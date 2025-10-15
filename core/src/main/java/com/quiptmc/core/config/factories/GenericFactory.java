package com.quiptmc.core.config.factories;

import com.quiptmc.core.config.ConfigObject;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

/**
 * A generic factory for creating instances of {@link ConfigObject} from JSON.
 * <p>
 * This factory uses reflection to instantiate objects of the specified type {@code T}.
 * The target class must have a public no-argument constructor and implement {@link ConfigObject#fromJson(JSONObject)}.
 * </p>
 * <p>
 * Usage requirements:
 * <ul>
 *   <li>{@code T} must be a subclass of {@link ConfigObject}.</li>
 *   <li>{@code T} must have a public no-argument constructor.</li>
 *   <li>{@code T} must implement {@link ConfigObject#fromJson(JSONObject)} to populate its fields from JSON.</li>
 * </ul>
 * </p>
 * <p>
 * Use this generic factory when your {@link ConfigObject} subclass can be constructed with a no-argument constructor
 * and populated via {@code fromJson}. If custom construction logic or additional parameters are required,
 * create a manual factory instead.
 * </p>
 *
 * @param <T> the type of {@link ConfigObject} to instantiate
 * @param type the class object representing {@code T}
 */
public record GenericFactory<T extends ConfigObject>(Class<T> type) implements ConfigObject.Factory<T> {

    @Override
    public String getClassName() {
        return type.getName();
    }

    @Override
    public T createFromJson(JSONObject json) {
        try {
            T instance = type.getConstructor().newInstance();
            instance.fromJson(json);
            return instance;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
                        throw new IllegalCallerException(getClassName() + " does not support generic factories. The class must have a public no-argument constructor.", e);
        }
    }
}
