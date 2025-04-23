package com.quiptmc.fabric.items.abstracts;

public class FoodSettings {

    private final int hunger;
    private final float saturationModifier;
    private final boolean isMeat;
    private final boolean isSnack;
    private final boolean isAlwaysEdible;
    private final boolean isFastToEat;

    private FoodSettings(Builder builder) {
        this.hunger = builder.hunger;
        this.saturationModifier = builder.saturationModifier;
        this.isMeat = builder.isMeat;
        this.isSnack = builder.isSnack;
        this.isAlwaysEdible = builder.isAlwaysEdible;
        this.isFastToEat = builder.isFastToEat;
    }

    public int getHunger() {
        return hunger;
    }

    public float getSaturationModifier() {
        return saturationModifier;
    }

    public boolean isMeat() {
        return isMeat;
    }

    public boolean isSnack() {
        return isSnack;
    }

    public boolean isAlwaysEdible() {
        return isAlwaysEdible;
    }

    public boolean isFastToEat() {
        return isFastToEat;
    }



    public static class Builder {
        private int hunger;
        private float saturationModifier;
        private boolean isMeat;
        private boolean isSnack;
        private boolean isAlwaysEdible;
        private boolean isFastToEat;

        public Builder(int hunger, float saturationModifier) {
            this.hunger = hunger;
            this.saturationModifier = saturationModifier;
        }

        public Builder setMeat(boolean isMeat) {
            this.isMeat = isMeat;
            return this;
        }

        public Builder setSnack(boolean isSnack) {
            this.isSnack = isSnack;
            return this;
        }

        public Builder setAlwaysEdible(boolean isAlwaysEdible) {
            this.isAlwaysEdible = isAlwaysEdible;
            return this;
        }

        public Builder setFastToEat(boolean isFastToEat) {
            this.isFastToEat = isFastToEat;
            return this;
        }

        public FoodSettings build() {
            return new FoodSettings(this);
        }
    }
}
