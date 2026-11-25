package live.qsmc.quipt.paper.assembler.definition;

/**
 * Represents a single room entry in an assembler definition.
 * Contains cost, weight, and terminator flag.
 */
public record RoomEntry(int cost, int weight, boolean terminator) {

}
