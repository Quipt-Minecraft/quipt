package com.quiptmc.core.data;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;

public interface JsonSerializable extends com.fasterxml.jackson.databind.JsonSerializable {

    default JSONObject json() {
        JSONObject json = new JSONObject();
        Class<?> currentClass = this.getClass();

        while (currentClass != null) {
            for (var field : currentClass.getDeclaredFields()) {
                if (Modifier.isPrivate(field.getModifiers())) continue;

                field.setAccessible(true);
                try {
                    Object value = field.get(this);
                    if (value == null)
                        continue;
                    if (value instanceof JsonSerializable data) {
                        if (json.has(field.getName())) {
                            continue; // Skip if already processed to prevent infinite recursion
                        }
                        value = data.json();
                    }
                    json.put(field.getName(), value);

                } catch (Exception e) {
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return json;
    }

    default void fromJson(JSONObject json) {
        Class<?> currentClass = this.getClass();

        while (currentClass != null) {
            for (var field : currentClass.getDeclaredFields()) {
                if (Modifier.isPrivate(field.getModifiers())) continue;
                field.setAccessible(true);
                if (json.has(field.getName())) {
                    try {
                        Object value = json.get(field.getName());
                        Class<?> fieldType = field.getType();
                        if (JsonSerializable.class.isAssignableFrom(fieldType) && value instanceof JSONObject obj) {
                            value = fieldType.getDeclaredConstructor(JSONObject.class).newInstance(obj);
                        }
                        setField(field, this, value);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to set field: " + field.getName(), e);
                    }
                }
            }
            currentClass = currentClass.getSuperclass();
        }
    }

    default Object get(String fieldName) {
        try {
            var field = this.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get field: " + fieldName, e);
        }
    }

    default void setField(Field field, Object target, Object value) throws IllegalAccessException {
        if (field.getType().isEnum() && value instanceof String) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum> enumClass = (Class<? extends Enum>) field.getType();
            field.set(target, convertStringToEnum((String) value, enumClass));
        } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
            if (value instanceof String) {
                field.set(target, Boolean.parseBoolean((String) value));
            } else {
                field.set(target, value);
            }
        } else {
            field.set(target, value);
        }
    }

    default <E extends Enum<E>> E convertStringToEnum(String value, Class<E> enumClass) {
        if (value == null) return null;
        return Enum.valueOf(enumClass, value.toUpperCase(Locale.ROOT));
    }

    @Override
    default void serialize(JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(this);
                jsonGenerator.writeObjectField(field.getName(), value);
            } catch (IllegalAccessException e) {
                throw new IOException("Failed to serialize field: " + field.getName(), e);
            }
        }
        jsonGenerator.writeEndObject();
    }

    @Override
    default void serializeWithType(JsonGenerator jsonGenerator, SerializerProvider serializerProvider, TypeSerializer typeSerializer) throws IOException {
        typeSerializer.writeTypePrefixForObject(this, jsonGenerator);
        serialize(jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForObject(this, jsonGenerator);
    }

}
