package live.qsmc.quipt.paper.assembler.model;

import org.bukkit.block.BlockFace;

/**
 * Represents a jigsaw block connector within a parsed room.
 * Contains position, facing direction, and jigsaw metadata.
 */
public record JigsawConnector(int x, int y, int z, BlockFace facing, String name, String target, String pool,
                              String joint, boolean finalState) {

    /**
     * Get the opposite direction of this connector's facing.
     * Used to determine which direction a new room should face.
     */
    public BlockFace getOppositeFacing() {
        return facing.getOppositeFace();
    }
}
