package live.qsmc.quipt.paper.assembler;

import live.qsmc.quipt.paper.assembler.definition.AssemblerDefinition;
import live.qsmc.quipt.paper.assembler.loader.AssemblerDefinitionLoader;
import live.qsmc.quipt.paper.assembler.model.AssemblyResult;
import live.qsmc.quipt.paper.assembler.model.ParsedRoom;
import live.qsmc.quipt.paper.assembler.model.PlacedPiece;
import live.qsmc.quipt.paper.assembler.placer.StructurePlacer;
import live.qsmc.quipt.paper.assembler.planner.AssemblerPlanner;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Public API for the assembler.
 * Provides generate, reload, and dryRun methods.
 */
public class AssemblerService {

    private static final Logger log = LoggerFactory.getLogger(AssemblerService.class);

    private final JavaPlugin plugin;
    private final AssemblerDefinitionLoader loader;
    private final StructurePlacer placer;

    public AssemblerService(JavaPlugin plugin) {
        this.plugin = plugin;
        this.loader = new AssemblerDefinitionLoader();
        this.placer = new StructurePlacer(plugin);

        // Set data folder from plugin
        loader.setDataFolder(plugin.getDataFolder().getParentFile());

        // Initial load
        reload();
    }

    /**
     * Generate a structure at the given location.
     *
     * @param definitionId the assembler definition ID (e.g., "ns:dungeon")
     * @param location the origin location
     * @param seed the random seed (0 for random)
     * @return a CompletableFuture with the assembly result
     */
    public CompletableFuture<AssemblyResult> generate(String definitionId, Location location, long seed) {
        CompletableFuture<AssemblyResult> future = new CompletableFuture<>();

        AssemblerDefinition definition = loader.getDefinition(definitionId);
        if (definition == null) {
            log.error("Assembler definition not found: {}", definitionId);
            future.completeExceptionally(new IllegalArgumentException("Definition not found: " + definitionId));
            return future;
        }

        long actualSeed = seed == 0 ? System.currentTimeMillis() : seed;
        Map<String, ParsedRoom> rooms = loader.getRooms();
        AssemblerPlanner planner = new AssemblerPlanner(rooms, actualSeed);

        AssemblyResult result = planner.plan(definition);
        if (result == null) {
            log.warn("Planning failed for definition: {}", definitionId);
            future.completeExceptionally(new IllegalStateException("Planning failed"));
            return future;
        }

        // Place the structure
        return placer.place(result, location);
    }

    /**
     * Dry run: plan a structure without placing it.
     *
     * @param definitionId the assembler definition ID
     * @param seed the random seed (0 for random)
     * @return a CompletableFuture with the planned result, or null if planning failed
     */
    public CompletableFuture<AssemblyResult> dryRun(String definitionId, long seed) {
        CompletableFuture<AssemblyResult> future = new CompletableFuture<>();

        AssemblerDefinition definition = loader.getDefinition(definitionId);
        if (definition == null) {
            log.error("Assembler definition not found: {}", definitionId);
            future.completeExceptionally(new IllegalArgumentException("Definition not found: " + definitionId));
            return future;
        }

        long actualSeed = seed == 0 ? System.currentTimeMillis() : seed;
        Map<String, ParsedRoom> rooms = loader.getRooms();
        AssemblerPlanner planner = new AssemblerPlanner(rooms, actualSeed);

        AssemblyResult result = planner.plan(definition);
        if (result == null) {
            log.warn("Planning failed for definition: {}", definitionId);
            future.completeExceptionally(new IllegalStateException("Planning failed"));
            return future;
        }

        logDryRun(result);
        future.complete(result);
        return future;
    }

    /**
     * Reload all assembler definitions and rooms.
     */
    public void reload() {
        loader.reload();
        log.info("Assembler definitions reloaded: {} definitions, {} rooms",
                loader.getDefinitions().size(), loader.getRooms().size());
    }

    /**
     * Get the loader for access to cached data.
     */
    public AssemblerDefinitionLoader getLoader() {
        return loader;
    }

    /**
     * Log the dry run results.
     */
    private void logDryRun(AssemblyResult result) {
        log.info("=== Dry Run Results ===");
        log.info("Pieces: {}", result.getPieceCount());
        log.info("Total cost: {}/{}", result.getTotalCost(), result.getTotalCost());
        log.info("Bounds: ({},{},{}) to ({},{},{})",
                result.getMinX(), result.getMinY(), result.getMinZ(),
                result.getMaxX(), result.getMaxY(), result.getMaxZ());
        log.info("Seed: {}", result.getSeed());
        for (PlacedPiece piece : result.getPieces()) {
            log.info("  - {} at ({},{},{}) cost={}",
                    piece.roomKey(), piece.worldX(), piece.worldY(), piece.worldZ(), piece.cost());
        }
        log.info("=== End Dry Run ===");
    }
}
