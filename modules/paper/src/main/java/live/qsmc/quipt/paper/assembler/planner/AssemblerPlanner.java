package live.qsmc.quipt.paper.assembler.planner;


import live.qsmc.quipt.paper.assembler.definition.*;
import live.qsmc.quipt.paper.assembler.model.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.structure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Core planning algorithm for the assembler.
 * Plans the full layout in memory before placing anything.
 */
public class AssemblerPlanner {

    private static final Logger log = LoggerFactory.getLogger(AssemblerPlanner.class);

    private final Map<String, ParsedRoom> roomCache;
    private final Random random;
    private final long seed;

    public AssemblerPlanner(Map<String, ParsedRoom> roomCache, long seed) {
        this.roomCache = roomCache;
        this.seed = seed;
        this.random = new Random(seed);
    }

    /**
     * Plan an assembly from the given definition.
     *
     * @param definition the assembler definition
     * @return the assembly result, or null if planning failed
     */
    public AssemblyResult plan(AssemblerDefinition definition) {
        // Check if start room exists
        ParsedRoom startRoom = roomCache.get(definition.startRoom());
        if (startRoom == null) {
            log.error("Start room not found: {}", definition.startRoom());
            return null;
        }

        // Check if start room fits in budget
        RoomEntry startEntry = definition.getRoomEntry(definition.startRoom());
        int startCost = startEntry != null ? startEntry.cost() : 0;
        if (startCost > definition.budget()) {
            log.error("Start room cost ({}) exceeds budget ({})", startCost, definition.budget());
            return null;
        }

        int budget = definition.budget();
        int maxDepth = definition.maxDepth();
        int maxPieces = definition.maxPieces();
        int cheapestTerminator = definition.getCheapestTerminatorCost();

        // Place start room at origin
        List<PlacedPiece> pieces = new ArrayList<>();
        PlacedPiece startPiece = new PlacedPiece(
                definition.startRoom(), 0, 0, 0,
                StructureRotation.NONE, Mirror.NONE,
                startCost, startRoom.width(), startRoom.height(), startRoom.length()
        );
        pieces.add(startPiece);

        int spent = startCost;
        int pieceCount = 1;

        // Build initial open connectors from start room
        Queue<OpenConnector> openConnectors = new LinkedList<>();
        for (JigsawConnector connector : startRoom.connectors()) {
            if (!connector.finalState()) {
                openConnectors.add(new OpenConnector(
                        connector.x(), connector.y(), connector.z(),
                        connector.facing(),
                        connector.target(),
                        1, // depth starts at 1 (start room is depth 0)
                        connector.pool()
                ));
            }
        }

        // Process open connectors
        while (!openConnectors.isEmpty() && pieceCount < maxPieces) {
            OpenConnector connector = openConnectors.poll();

            // Check depth limit
            boolean atMaxDepth = connector.depth() >= maxDepth;

            // Build candidate list
            List<String> candidates = buildCandidates(connector, definition, atMaxDepth);

            // Shuffle by weight
            shuffleByWeight(candidates, definition);

            boolean placed = false;

            for (String candidateKey : candidates) {
                ParsedRoom candidateRoom = roomCache.get(candidateKey);
                if (candidateRoom == null) {
                    continue;
                }

                RoomEntry candidateEntry = definition.getRoomEntry(candidateKey);
                if (candidateEntry == null) {
                    continue;
                }

                int candidateCost = candidateEntry.cost();

                // Try to find a valid position and rotation
                PlacementAttempt attempt = findPlacement(
                        connector, candidateRoom, candidateEntry, pieces, spent, budget,
                        openConnectors.size(), cheapestTerminator, atMaxDepth
                );

                if (attempt != null) {
                    pieces.add(attempt.piece);
                    spent += attempt.cost;
                    pieceCount++;

                    // Add new open connectors from placed piece
                    for (JigsawConnector newConnector : candidateRoom.connectors()) {
                        if (!newConnector.finalState()) {
                            int newDepth = connector.depth() + 1;
                            if (newDepth <= maxDepth) {
                                int wx = attempt.piece.worldX() + newConnector.x();
                                int wy = attempt.piece.worldY() + newConnector.y();
                                int wz = attempt.piece.worldZ() + newConnector.z();
                                openConnectors.add(new OpenConnector(
                                        wx, wy, wz,
                                        newConnector.facing(),
                                        newConnector.target(),
                                        newDepth,
                                        newConnector.pool()
                                ));
                            }
                        }
                    }

                    placed = true;
                    break;
                }
            }

            // If no candidate fit, try terminator
            if (!placed) {
                PlacementAttempt terminatorAttempt = findTerminatorPlacement(
                        connector, definition, pieces, spent, budget
                );
                if (terminatorAttempt != null) {
                    pieces.add(terminatorAttempt.piece);
                    spent += terminatorAttempt.cost;
                    pieceCount++;
                } else {
                    // Can't place a terminator — abort
                    log.warn("Cannot place terminator for connector at ({},{},{}) — aborting generation",
                            connector.worldX(), connector.worldY(), connector.worldZ());
                    return null;
                }
            }
        }

        // Assert budget invariant
        assert spent <= budget : "Budget invariant violated: spent " + spent + " > budget " + budget;

        log.info("Planned assembly: {} pieces, cost {}/{}", pieceCount, spent, budget);
        return new AssemblyResult(pieces, spent, seed);
    }

    /**
     * Build a list of candidate room keys for a connector.
     */
    private List<String> buildCandidates(OpenConnector connector, AssemblerDefinition definition,
                                          boolean atMaxDepth) {
        List<String> candidates = new ArrayList<>();

        if (atMaxDepth) {
            // Only allow terminators at max depth
            return definition.getTerminatorRoomKeys();
        }

        // Find rooms that have a jigsaw connector matching this connector's target name
        for (Map.Entry<String, RoomEntry> entry : definition.rooms().entrySet()) {
            String roomKey = entry.getKey();
            ParsedRoom room = roomCache.get(roomKey);
            if (room == null) {
                continue;
            }

            // Check if this room has a connector whose name matches the connector's target
            for (JigsawConnector roomConnector : room.connectors()) {
                if (connector.targetName().equals(roomConnector.name())) {
                    candidates.add(roomKey);
                    break;
                }
            }
        }

        return candidates;
    }

    /**
     * Shuffle candidates by weight using the seeded random.
     */
    private void shuffleByWeight(List<String> candidates, AssemblerDefinition definition) {
        // Build weighted list
        List<String> weighted = new ArrayList<>();
        for (String key : candidates) {
            RoomEntry entry = definition.getRoomEntry(key);
            int weight = entry != null ? entry.weight() : 1;
            for (int i = 0; i < weight; i++) {
                weighted.add(key);
            }
        }

        // Shuffle and deduplicate
        Collections.shuffle(weighted, random);
        List<String> shuffled = new ArrayList<>(new LinkedHashSet<>(weighted));
        candidates.clear();
        candidates.addAll(shuffled);
    }

    /**
     * Try to find a valid placement for a candidate room.
     */
    private PlacementAttempt findPlacement(OpenConnector connector, ParsedRoom room,
                                            RoomEntry entry, List<PlacedPiece> pieces,
                                            int spent, int budget, int openConnectorCount,
                                            int cheapestTerminator, boolean atMaxDepth) {
        int cost = entry.cost();

        // Budget check: spent + cost + (remaining open connectors) * cheapest terminator <= budget
        // After placing this room, we'll have: openConnectorCount - 1 (this connector) + new connectors
        // For a conservative check, assume at least 0 new connectors
        int remainingOpen = Math.max(0, openConnectorCount - 1);
        if (spent + cost + remainingOpen * cheapestTerminator > budget) {
            return null;
        }

        // Try different rotations and mirrors
        StructureRotation[] rotations = StructureRotation.values();
        Mirror[] mirrors = Mirror.values();

        for (StructureRotation rotation : rotations) {
            for (Mirror mirror : mirrors) {
                // Calculate placement position
                int dx = 0, dy = 0, dz = 0;
                BlockFace facing = connector.facing();
                if (facing == BlockFace.EAST) dx = 1;
                else if (facing == BlockFace.WEST) dx = -1;
                else if (facing == BlockFace.DOWN) dy = -1;
                else if (facing == BlockFace.UP) dy = 1;
                else if (facing == BlockFace.SOUTH) dz = 1;
                else if (facing == BlockFace.NORTH) dz = -1;

                int wx = connector.worldX() + dx;
                int wy = connector.worldY() + dy;
                int wz = connector.worldZ() + dz;

                // Check collision
                if (!hasCollision(wx, wy, wz, room.width(), room.height(), room.length(), pieces)) {
                    PlacedPiece piece = new PlacedPiece(
                            room.structureKey(), wx, wy, wz,
                            rotation, mirror, cost,
                            room.width(), room.height(), room.length()
                    );
                    return new PlacementAttempt(piece, cost);
                }
            }
        }

        return null;
    }

    /**
     * Find a terminator placement for a connector.
     */
    private PlacementAttempt findTerminatorPlacement(OpenConnector connector, AssemblerDefinition definition,
                                                      List<PlacedPiece> pieces, int spent, int budget) {
        List<String> terminators = definition.getTerminatorRoomKeys();
        if (terminators.isEmpty()) {
            return null;
        }

        // Find cheapest terminator that fits
        PlacementAttempt best = null;
        for (String terminatorKey : terminators) {
            ParsedRoom room = roomCache.get(terminatorKey);
            if (room == null) {
                continue;
            }

            RoomEntry entry = definition.getRoomEntry(terminatorKey);
            if (entry == null) {
                continue;
            }

            int cost = entry.cost();
            if (spent + cost > budget) {
                continue;
            }

            // Try to place
            int dx = 0, dy = 0, dz = 0;
            BlockFace facing = connector.facing();
            if (facing == BlockFace.EAST) dx = 1;
            else if (facing == BlockFace.WEST) dx = -1;
            else if (facing == BlockFace.DOWN) dy = -1;
            else if (facing == BlockFace.UP) dy = 1;
            else if (facing == BlockFace.SOUTH) dz = 1;
            else if (facing == BlockFace.NORTH) dz = -1;

            int wx = connector.worldX() + dx;
            int wy = connector.worldY() + dy;
            int wz = connector.worldZ() + dz;

            if (!hasCollision(wx, wy, wz, room.width(), room.height(), room.length(), pieces)) {
                PlacedPiece piece = new PlacedPiece(
                        room.structureKey(), wx, wy, wz,
                        StructureRotation.NONE, Mirror.NONE,
                        cost, room.width(), room.height(), room.length()
                );
                if (best == null || cost < best.cost) {
                    best = new PlacementAttempt(piece, cost);
                }
            }
        }

        return best;
    }

    /**
     * Check if placing a room at the given position would collide with existing pieces.
     */
    private boolean hasCollision(int wx, int wy, int wz, int width, int height, int length,
                                  List<PlacedPiece> pieces) {
        for (PlacedPiece piece : pieces) {
            int dx = piece.worldX() - wx;
            int dy = piece.worldY() - wy;
            int dz = piece.worldZ() - wz;

            if (Math.abs(dx) < Math.max(piece.width(), width) &&
                Math.abs(dy) < Math.max(piece.height(), height) &&
                Math.abs(dz) < Math.max(piece.length(), length)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Internal class for a placement attempt result.
     */
    private static class PlacementAttempt {
        final PlacedPiece piece;
        final int cost;

        PlacementAttempt(PlacedPiece piece, int cost) {
            this.piece = piece;
            this.cost = cost;
        }
    }
}
