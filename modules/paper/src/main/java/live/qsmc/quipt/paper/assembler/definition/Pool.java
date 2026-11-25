package live.qsmc.quipt.paper.assembler.definition;

import java.util.List;

/**
 * Represents a named pool of room keys.
 * Used to map jigsaw block pool references to actual rooms.
 */
public record Pool(String name, List<String> roomKeys) {

}
