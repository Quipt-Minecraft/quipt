package com.quiptmc.fabric.client.datagen;

import com.quiptmc.core.data.registries.Registries;
import com.quiptmc.core.data.registries.Registry;
import com.quiptmc.fabric.blocks.QuiptBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class QuiptModelProvider extends FabricModelProvider {

    private final Registry<Model> MODELS = Registries.register("models", Model.class);
    private final Model CUBE_ALL = new Model(
            Optional.of(Identifier.of("minecraft", "block/cube_all")),
            Optional.empty(),
            TextureKey.ALL);

    public QuiptModelProvider(FabricDataOutput output) {
        super(output);
//        createModel("cube_all.json", "", TextureKey.ALL);
    }

    private Model createModel(final String id, final @NotNull String variant, final TextureKey... textureKeys) {
        Model model = new Model(Optional.of(Identifier.of("quipt", "block/%s%s".formatted(id, variant))), variant.isEmpty() ? Optional.empty() : Optional.of(variant), textureKeys);
        MODELS.register(id, model);
        return model;
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        QuiptBlocks.blocks().forEach((block) -> {
            generateModelForBlock(block.block(), blockStateModelGenerator);
        });
    }

    private Model determineModelType(Block block) {
//        // Example logic - extend this to support more block types
//        // You could use instanceof checks or block properties
        return CUBE_ALL;
    }

    private void generateModelForBlock(Block block, BlockStateModelGenerator generator) {
        // Choose appropriate model based on block properties or metadata
        Model model = determineModelType(block);

        // Generate appropriate texture mapping
        TextureMap textureMap = generateTextureMap(block, model);

        final Identifier modelId = model.upload(block, textureMap, generator.modelCollector);
        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
        generator.registerParentedItemModel(block, modelId);
    }

    private TextureMap generateTextureMap(Block block, Model model) {
        // Default to simple texture mapping
        return TextureMap.all(block);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item model generation code
    }

}
