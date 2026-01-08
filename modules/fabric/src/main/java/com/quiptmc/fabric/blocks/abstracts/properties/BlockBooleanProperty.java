package com.quiptmc.fabric.blocks.abstracts.properties;

import java.util.List;
import java.util.Optional;

public class BlockBooleanProperty extends BlockProperty<Boolean> {
    private static final List<Boolean> values = List.of(true, false);
    private static final int TRUE_ORDINAL = 0;
    private static final int FALSE_ORDINAL = 1;

    private BlockBooleanProperty(String name, boolean defaultValue) {
        super(name, defaultValue, Boolean.class);
    }

    @Override
    public List<Boolean> getValues() {
        return values;
    }


    public static BlockBooleanProperty of(String name, boolean defaultValue) {
        return new BlockBooleanProperty(name, defaultValue);
    }

    @Override
    public Optional<Boolean> parse(String name) {
        return switch (name) {
            case "true" -> Optional.of(true);
            case "false" -> Optional.of(false);
            default -> Optional.empty();
        };
    }

    public String name(Boolean boolean_) {
        return boolean_.toString();
    }

    public int ordinal(Boolean boolean_) {
        return boolean_ ? 0 : 1;
    }
}
