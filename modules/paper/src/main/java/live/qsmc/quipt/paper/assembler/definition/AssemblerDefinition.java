package live.qsmc.quipt.paper.assembler.definition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents an assembler definition parsed from a JSON file.
 * Contains start room, budget, constraints, room entries, and optional pools.
 */
public record AssemblerDefinition(String id, String startRoom, int budget, int maxDepth, int maxPieces,
                                  Map<String, RoomEntry> rooms, Map<String, Pool> pools) {

    public AssemblerDefinition(String id, String startRoom, int budget, int maxDepth,
                               int maxPieces, Map<String, RoomEntry> rooms, Map<String, Pool> pools) {
        this.id = id;
        this.startRoom = startRoom;
        this.budget = budget;
        this.maxDepth = maxDepth;
        this.maxPieces = maxPieces;
        this.rooms = rooms != null ? Collections.unmodifiableMap(rooms) : Collections.emptyMap();
        this.pools = pools != null ? Collections.unmodifiableMap(pools) : Collections.emptyMap();
    }

    /**
     * Get the RoomEntry for a given room key, or null if not found.
     */
    public RoomEntry getRoomEntry(String roomKey) {
        return rooms.get(roomKey);
    }

    /**
     * Get the cheapest terminator cost from all rooms.
     * Returns Integer.MAX_VALUE if no terminator is defined.
     */
    public int getCheapestTerminatorCost() {
        int cheapest = Integer.MAX_VALUE;
        for (RoomEntry entry : rooms.values()) {
            if (entry.terminator() && entry.cost() < cheapest) {
                cheapest = entry.cost();
            }
        }
        return cheapest;
    }

    /**
     * Get all room keys that are terminators.
     */
    public List<String> getTerminatorRoomKeys() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, RoomEntry> entry : rooms.entrySet()) {
            if (entry.getValue().terminator()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    /**
     * Get all room keys that are NOT terminators.
     */
    public List<String> getNonTerminatorRoomKeys() {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, RoomEntry> entry : rooms.entrySet()) {
            if (!entry.getValue().terminator()) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
