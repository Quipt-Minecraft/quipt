package com.quiptmc.fabric.blocks.abstracts.properties;

import net.minecraft.state.property.Property;

import java.util.List;
import java.util.Optional;

public final class StringProperty extends Property<String> {

    private final String[] values;
    private final String defaultValue;

    private StringProperty(String name, List<String> values, String defaultValue) {
        super(name, String.class);
        if (!values.contains(defaultValue)) {
            throw new IllegalArgumentException("Value " + defaultValue + " is not in the list of values for " + name);
        } else {
            this.values = values.toArray(new String[0]);
            this.defaultValue = defaultValue;
        }
    }

    public static StringProperty of(String name, List<String> values, String defaultValue) {
        return new StringProperty(name, values, defaultValue);
    }

    @Override
    public List<String> getValues() {
        return List.of(values);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            return object instanceof StringProperty stringProperty && super.equals(object) ? this.values.equals(stringProperty.values) : false;
        }
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + this.values.hashCode();
    }

    @Override
    public Optional<String> parse(String name) {
        return getValues().contains(name) ? Optional.of(name) : Optional.empty();
    }

    public String name(String string) {
        return string;
    }

    public int ordinal(String string) {
        return getValues().contains(string) ? getValues().indexOf(string) : -1;
    }
}

