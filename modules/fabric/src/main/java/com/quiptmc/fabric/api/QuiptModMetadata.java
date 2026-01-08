package com.quiptmc.fabric.api;

import net.fabricmc.loader.api.metadata.ModMetadata;

public record QuiptModMetadata(FabricIntegration integration, ModMetadata metadata) {
}
