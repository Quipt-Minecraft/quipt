package live.qsmc.quipt.paper.assembler.model;

import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Represents a placed piece in the assembly plan.
 * Contains room key, world position, rotation, mirror, and cost.
 */
public record PlacedPiece(String roomKey, int worldX, int worldY, int worldZ, StructureRotation rotation, Mirror mirror,
                          int cost, int width, int height, int length) {

    /**
     * Get the world location for placing this piece.
     */
    public Location getLocation(World world, Location origin) {
        return new Location(
                world,
                origin.getX() + worldX,
                origin.getY() + worldY,
                origin.getZ() + worldZ
        );
    }

    /**
     * Check if this piece's AABB overlaps with another piece.
     */
    public boolean overlaps(PlacedPiece other) {
        int dx = other.worldX - worldX;
        int dy = other.worldY - worldY;
        int dz = other.worldZ - worldZ;
        return !(Math.abs(dx) >= Math.max(width, other.width) ||
                Math.abs(dy) >= Math.max(height, other.height) ||
                Math.abs(dz) >= Math.max(length, other.length));
    }
}
