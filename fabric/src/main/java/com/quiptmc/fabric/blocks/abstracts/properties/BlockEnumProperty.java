package com.quiptmc.fabric.blocks.abstracts.properties;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.StringIdentifiable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BlockEnumProperty<T extends Enum<T> & StringIdentifiable> extends BlockProperty<T> {
    private final List<T> values;
    private final Map<String, T> byName;
    private final int[] enumOrdinalToPropertyOrdinal;

    private BlockEnumProperty(String name, Class<T> type, List<T> values, T defaultValue) {
        super(name, defaultValue, type);
        if (values.isEmpty()) {
            throw new IllegalArgumentException("Trying to make empty EnumProperty '" + name + "'");
        } else {
            this.values = List.copyOf(values);
            T[] enums = (T[])type.getEnumConstants();
            this.enumOrdinalToPropertyOrdinal = new int[enums.length];

            for (T enum_ : enums) {
                this.enumOrdinalToPropertyOrdinal[enum_.ordinal()] = values.indexOf(enum_);
            }

            ImmutableMap.Builder<String, T> builder = ImmutableMap.builder();

            for (T enum2 : values) {
                String string = enum2.asString();
                builder.put(string, enum2);
            }

            this.byName = builder.buildOrThrow();
        }
    }


    @Override
    public List<T> getValues() {
        return this.values;
    }

    @Override
    public Optional<T> parse(String name) {
        return Optional.ofNullable((T) this.byName.get(name));
    }

    public String name(T enum_) {
        return enum_.asString();
    }

    public int ordinal(T enum_) {
        return this.enumOrdinalToPropertyOrdinal[enum_.ordinal()];
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            return object instanceof BlockEnumProperty<?> enumProperty && super.equals(object) && this.values.equals(enumProperty.values);
        }
    }

    @Override
    public int computeHashCode() {
        int i = super.computeHashCode();
        return 31 * i + this.values.hashCode();
    }

    public static <T extends Enum<T> & StringIdentifiable> BlockEnumProperty<T> of(String name, Class<T> type, T defaultValue) {
        
        return of(name, type, defaultValue, type.getEnumConstants());
    }

    

   
    @SafeVarargs
    public static <T extends Enum<T> & StringIdentifiable> BlockEnumProperty<T> of(String name, Class<T> type, T defaultValue, T... values) {
        return of(name, type, List.of(values), defaultValue);
    }
    
    public static <T extends Enum<T> & StringIdentifiable> BlockEnumProperty<T> of(String name, Class<T> type, List<T> values, T defaultValue) {
        return new BlockEnumProperty<>(name, type, values, defaultValue);
    }
}