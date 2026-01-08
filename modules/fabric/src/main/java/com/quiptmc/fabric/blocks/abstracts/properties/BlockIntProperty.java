package com.quiptmc.fabric.blocks.abstracts.properties;

import it.unimi.dsi.fastutil.ints.IntImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class BlockIntProperty extends BlockProperty<Integer> {

    private final IntImmutableList values;
    private final int min;
    private final int max;

    private BlockIntProperty(String name, int min, int max, int defaultValue) {
        super(name, defaultValue, Integer.class);
        if (min < 0) {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        } else if (max <= min) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        } else {
            this.min = min;
            this.max = max;
            this.values = IntImmutableList.toList(IntStream.range(min, max + 1));
        }
    }

    @Override
    public List<Integer> getValues() {
        return this.values;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else {
            return object instanceof BlockIntProperty intProperty && super.equals(object) && this.values.equals(intProperty.values);
        }
    }

    @Override
    public int computeHashCode() {
        return 31 * super.computeHashCode() + this.values.hashCode();
    }

    /**
     * Creates an integer property.
     *
     * <p>Note that this method computes all possible values.
     *
     * @throws IllegalArgumentException if {@code 0 <= min < max} is not
     * satisfied
     *
     * @param max the maximum value the property contains
     * @param name the name of the property.
     * @param min the minimum value the property contains
     */
    public static BlockIntProperty of(String name, int min, int max, int defaultValue) {
        return new BlockIntProperty(name, min, max, defaultValue);
    }

    @Override
    public Optional<Integer> parse(String name) {
        try {
            int i = Integer.parseInt(name);
            return i >= this.min && i <= this.max ? Optional.of(i) : Optional.empty();
        } catch (NumberFormatException var3) {
            return Optional.empty();
        }
    }

    public String name(Integer integer) {
        return integer.toString();
    }

    public int ordinal(Integer integer) {
        return integer <= this.max ? integer - this.min : -1;
    }
}
