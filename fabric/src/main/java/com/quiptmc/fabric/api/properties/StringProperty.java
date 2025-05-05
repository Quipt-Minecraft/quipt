package com.quiptmc.fabric.api.properties;

import net.minecraft.state.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class StringProperty extends Property<String> {

    public static final List<String> values = new ArrayList<>();

    private StringProperty(String name) {
        super(name, String.class);
        values.add(name);
        values.add("level1");

    }

    @Override
    public List<String> getValues() {
        return values;
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + getValues().hashCode();
    }

    /**
     * Creates an integer property.
     *
     * <p>Note that this method computes all possible values.
     *
     * @throws IllegalArgumentException if {@code 0 <= min < max} is not
     * satisfied
     *
     * @param name the name of the property; see {@linkplain Property
     * name}
     */
    public static StringProperty of(String name) {
        return new StringProperty(name);
    }

    @Override
    public Optional<String> parse(String name) {
        return Optional.ofNullable(name);
    }

    public String name(String key) {
        return key;
    }

    public int ordinal(String string) {
        return 1;
    }
}

