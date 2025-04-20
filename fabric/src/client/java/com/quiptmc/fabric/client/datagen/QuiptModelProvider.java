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

    //    private static final Model CUBE_ALL = createModel("cube_all", "", TextureKey.ALL);
    private final Registry<Model> MODELS = Registries.register("models", Model.class);

    public QuiptModelProvider(FabricDataOutput output) {
        super(output);
//        createModel("cube_all", "", TextureKey.ALL);
    }
    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        QuiptBlocks.blocks().forEach((block) -> {
            generateModelForBlock(block, blockStateModelGenerator);
        });
    }

    private void generateModelForBlock(Block block, BlockStateModelGenerator generator) {
        // Choose appropriate model based on block properties or metadata
//        Model model = determineModelType(block);

        // Generate appropriate texture mapping
//        TextureMap textureMap = generateTextureMap(block, model);

//        final Identifier modelId = model.upload(block, textureMap, generator.modelCollector);
//        generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, modelId));
//        generator.registerParentedItemModel(block, modelId);
    }

//    private Model determineModelType(Block block) {
//        // Example logic - extend this to support more block types
//        // You could use instanceof checks or block properties
////        return MODELS.getCubeAll();
//    }

    private TextureMap generateTextureMap(Block block, Model model) {
        // Default to simple texture mapping
        return TextureMap.all(block);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Item model generation code
    }
}
