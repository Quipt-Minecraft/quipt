package live.qsmc.quipt.paper.assembler.model;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;

/**
 * Represents an open jigsaw connector in the world during planning.
 * Tracks world position, facing, target name, and depth.
 */
public record OpenConnector(int worldX, int worldY, int worldZ, BlockFace facing, String targetName, int depth,
                            String pool) {

    /**
     * Get the world location where a new room should be placed.
     * The new room's connector should face opposite to this connector's facing.
     */
    public Location getPlacementLocation(Location origin) {
        int dx = 0, dy = 0, dz = 0;
        if (facing == BlockFace.EAST) dx = 1;
        else if (facing == BlockFace.WEST) dx = -1;
        else if (facing == BlockFace.DOWN) dy = -1;
        else if (facing == BlockFace.UP) dy = 1;
        else if (facing == BlockFace.SOUTH) dz = 1;
        else if (facing == BlockFace.NORTH) dz = -1;
        return new Location(
                origin.getWorld(),
                origin.getX() + worldX + dx,
                origin.getY() + worldY + dy,
                origin.getZ() + worldZ + dz,
                origin.getYaw(),
                origin.getPitch()
        );
    }
}
