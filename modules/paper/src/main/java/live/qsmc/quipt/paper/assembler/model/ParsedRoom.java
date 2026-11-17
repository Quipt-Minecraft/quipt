package live.qsmc.quipt.paper.assembler.model;

import java.util.List;

/**
 * Represents a parsed structure room from an .nbt file.
 * Contains dimensions, jigsaw connectors, and bounding box info.
 */
public record ParsedRoom(String structureKey, int width, int height, int length, List<JigsawConnector> connectors) {

    /**
     * Check if this room's AABB overlaps with another room at the given offset.
     *
     * @param otherWidth  width of the other room
     * @param otherHeight height of the other room
     * @param otherLength length of the other room
     * @param otherX      X offset of the other room relative to this room
     * @param otherY      Y offset of the other room relative to this room
     * @param otherZ      Z offset of the other room relative to this room
     * @return true if the bounding boxes overlap
     */
    public boolean overlaps(int otherWidth, int otherHeight, int otherLength,
                            int otherX, int otherY, int otherZ) {
        return !(otherX >= width || otherX + otherWidth <= 0 ||
                otherY >= height || otherY + otherHeight <= 0 ||
                otherZ >= length || otherZ + otherLength <= 0);
    }
}
