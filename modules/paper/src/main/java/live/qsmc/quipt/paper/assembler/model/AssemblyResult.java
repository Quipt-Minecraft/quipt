package live.qsmc.quipt.paper.assembler.model;

import java.util.List;

/**
 * Result of an assembly planning operation.
 * Contains placed pieces, total cost, bounding box, and seed.
 */
public class AssemblyResult {

    private final List<PlacedPiece> pieces;
    private final int totalCost;
    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;
    private final long seed;

    public AssemblyResult(List<PlacedPiece> pieces, int totalCost, long seed) {
        this.pieces = pieces;
        this.totalCost = totalCost;
        this.seed = seed;
        this.minX = Integer.MAX_VALUE;
        this.minY = Integer.MAX_VALUE;
        this.minZ = Integer.MAX_VALUE;
        this.maxX = Integer.MIN_VALUE;
        this.maxY = Integer.MIN_VALUE;
        this.maxZ = Integer.MIN_VALUE;
        for (PlacedPiece piece : pieces) {
            int px = piece.worldX();
            int py = piece.worldY();
            int pz = piece.worldZ();
            int px2 = px + piece.width();
            int py2 = py + piece.height();
            int pz2 = pz + piece.length();
            if (px < minX) minX = px;
            if (py < minY) minY = py;
            if (pz < minZ) minZ = pz;
            if (px2 > maxX) maxX = px2;
            if (py2 > maxY) maxY = py2;
            if (pz2 > maxZ) maxZ = pz2;
        }
        if (pieces.isEmpty()) {
            this.minX = 0;
            this.minY = 0;
            this.minZ = 0;
            this.maxX = 0;
            this.maxY = 0;
            this.maxZ = 0;
        }
    }

    public List<PlacedPiece> getPieces() {
        return pieces;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public long getSeed() {
        return seed;
    }

    public int getPieceCount() {
        return pieces.size();
    }
}
