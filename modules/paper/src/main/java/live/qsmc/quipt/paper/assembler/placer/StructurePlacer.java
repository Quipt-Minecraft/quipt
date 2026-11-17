package live.qsmc.quipt.paper.assembler.placer;

import live.qsmc.quipt.paper.assembler.model.AssemblyResult;
import live.qsmc.quipt.paper.assembler.model.PlacedPiece;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * Places planned pieces in the world, in batches per tick.
 */
public class StructurePlacer {

    private static final Logger log = LoggerFactory.getLogger(StructurePlacer.class);

    private final org.bukkit.plugin.java.JavaPlugin plugin;
    private final int piecesPerTick;

    public StructurePlacer(org.bukkit.plugin.java.JavaPlugin plugin) {
        this(plugin, 8);
    }

    public StructurePlacer(org.bukkit.plugin.java.JavaPlugin plugin, int piecesPerTick) {
        this.plugin = plugin;
        this.piecesPerTick = piecesPerTick;
    }

    /**
     * Place all pieces from the assembly result in the world.
     *
     * @param result the assembly result
     * @param origin the origin location in the world
     * @return a CompletableFuture that completes when all pieces are placed
     */
    public CompletableFuture<AssemblyResult> place(AssemblyResult result, Location origin) {
        CompletableFuture<AssemblyResult> future = new CompletableFuture<>();
        List<PlacedPiece> pieces = result.getPieces();
        World world = origin.getWorld();

        if (pieces.isEmpty()) {
            future.complete(result);
            return future;
        }

        log.info("Placing {} pieces at ({}, {}, {})", pieces.size(),
                origin.getX(), origin.getY(), origin.getZ());

        // Start batch placement
        placeBatch(result, origin, pieces, 0, future);

        return future;
    }

    /**
     * Place a batch of pieces, scheduling the next batch if needed.
     */
    private void placeBatch(AssemblyResult result, Location origin,
                            List<PlacedPiece> pieces, int index,
                            CompletableFuture<AssemblyResult> future) {
        World world = origin.getWorld();
        org.bukkit.structure.StructureManager manager = org.bukkit.Bukkit.getStructureManager();
        Random random = new Random(result.getSeed());

        int end = Math.min(index + piecesPerTick, pieces.size());

        for (int i = index; i < end; i++) {
            PlacedPiece piece = pieces.get(i);

            // Parse the room key into namespace:key
            String[] parts = piece.roomKey().split(":", 2);
            if (parts.length != 2) {
                log.warn("Invalid room key: {}", piece.roomKey());
                continue;
            }

            NamespacedKey key = new NamespacedKey(parts[0], parts[1]);

            // Load the structure
            org.bukkit.structure.Structure structure = manager.loadStructure(key);
            if (structure == null) {
                log.warn("Failed to load structure: {}", piece.roomKey());
                continue;
            }

            // Calculate placement location
            Location location = new Location(
                    world,
                    origin.getX() + piece.worldX(),
                    origin.getY() + piece.worldY(),
                    origin.getZ() + piece.worldZ()
            );

            // Ensure chunk is loaded
            Chunk chunk = location.getChunk();
            if (!chunk.isLoaded()) {
                chunk.load();
            }

            // Convert rotation/mirror enums
            StructureRotation rotation = piece.rotation();
            Mirror mirror = piece.mirror();

            // Place the structure
            try {
                structure.place(location, false, rotation, mirror, -1, 1.0f, random);
            } catch (Exception e) {
                log.error("Failed to place structure {} at ({}, {}, {}): {}",
                        piece.roomKey(), location.getX(), location.getY(), location.getZ(),
                        e.getMessage());
            }
        }

        if (end < pieces.size()) {
            // Schedule next batch on next tick
            Bukkit.getScheduler().runTaskLater(
                    plugin,
                    () -> placeBatch(result, origin, pieces, end, future),
                    1L
            );
        } else {
            // All pieces placed
            log.info("Finished placing {} pieces", pieces.size());
            future.complete(result);
        }
    }
}
