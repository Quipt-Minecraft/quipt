package com.quiptmc.minecraft.api.world;

import org.jetbrains.annotations.NotNull;

public interface QuiptWorld<T> {

    T generate();


    class Builder {

        private final String name;
        private String environment = "NORMAL";
        private String type = "NORMAL";
        private QuiptChunkGenerator chunkGenerator;
        private boolean generateStructures = true;
        private boolean generateSpawn = true;
        private boolean generateBedrock = true;
        private boolean generateCaves = true;
        private boolean generateSurface = true;
        private boolean generateNoise = true;
        private boolean generateVoid = false;
        private boolean generateBiome = true;


        public Builder(String name){
            this.name = name;
        }

        public @NotNull String name() {
            return name;
        }

        public @NotNull String environment() {
            return environment;
        }

        public @NotNull String type() {
            return type;
        }

        public @NotNull QuiptChunkGenerator chunkGenerator() {
            return chunkGenerator;
        }

        public boolean generateStructures() {
            return generateStructures;
        }

        public boolean generateSpawn() {
            return generateSpawn;
        }

        public boolean generateBedrock() {
            return generateBedrock;
        }

        public boolean generateCaves() {
            return generateCaves;
        }

        public boolean generateSurface() {
            return generateSurface;
        }

        public boolean generateNoise() {
            return generateNoise;
        }

        public boolean generateVoid() {
            return generateVoid;
        }

        public boolean generateBiome() {
            return generateBiome;
        }

        public Builder environment(@NotNull String environment) {
            this.environment = environment;
            return this;
        }

        public Builder type(@NotNull String type) {
            this.type = type;
            return this;
        }

        public Builder chunkGenerator(@NotNull QuiptChunkGenerator chunkGenerator) {
            this.chunkGenerator = chunkGenerator;
            return this;
        }

        public Builder generateStructures(boolean generateStructures) {
            this.generateStructures = generateStructures;
            return this;
        }

        public Builder generateSpawn(boolean generateSpawn) {
            this.generateSpawn = generateSpawn;
            return this;
        }

        public Builder generateBedrock(boolean generateBedrock) {
            this.generateBedrock = generateBedrock;
            return this;
        }

        public Builder generateCaves(boolean generateCaves) {
            this.generateCaves = generateCaves;
            return this;
        }

        public Builder generateSurface(boolean generateSurface) {
            this.generateSurface = generateSurface;
            return this;
        }

        public Builder generateNoise(boolean generateNoise) {
            this.generateNoise = generateNoise;
            return this;
        }

        public Builder generateVoid(boolean generateVoid) {
            this.generateVoid = generateVoid;
            return this;
        }

        public Builder generateBiome(boolean generateBiome) {
            this.generateBiome = generateBiome;
            return this;
        }

    }
}
