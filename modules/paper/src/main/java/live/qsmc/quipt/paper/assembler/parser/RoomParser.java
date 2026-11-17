package live.qsmc.quipt.paper.assembler.parser;

import live.qsmc.quipt.paper.assembler.model.JigsawConnector;
import live.qsmc.quipt.paper.assembler.model.ParsedRoom;
import org.bukkit.block.BlockFace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses structure .nbt files into ParsedRoom objects using reflection to access
 * the tr7zw NBT-API plugin at runtime. No compile-time dependency required.
 */
public class RoomParser {

    private static final Logger log = LoggerFactory.getLogger(RoomParser.class);
    private static final String JIGSAW_BLOCK_ID = "minecraft:jigsaw";

    private static final Map<String, BlockFace> ORIENTATION_TO_FACE = new HashMap<>();

    static {
        ORIENTATION_TO_FACE.put("north_up", BlockFace.NORTH);
        ORIENTATION_TO_FACE.put("south_up", BlockFace.SOUTH);
        ORIENTATION_TO_FACE.put("east_up", BlockFace.EAST);
        ORIENTATION_TO_FACE.put("west_up", BlockFace.WEST);
        ORIENTATION_TO_FACE.put("up_east", BlockFace.UP);
        ORIENTATION_TO_FACE.put("up_south", BlockFace.UP);
        ORIENTATION_TO_FACE.put("up_west", BlockFace.UP);
        ORIENTATION_TO_FACE.put("up_north", BlockFace.UP);
        ORIENTATION_TO_FACE.put("down_east", BlockFace.DOWN);
        ORIENTATION_TO_FACE.put("down_south", BlockFace.DOWN);
        ORIENTATION_TO_FACE.put("down_west", BlockFace.DOWN);
        ORIENTATION_TO_FACE.put("down_north", BlockFace.DOWN);
    }

    private Class<?> nbtClass;
    private Class<?> nbtCompoundClass;
    private Class<?> nbtCompoundListClass;
    private Method readFileMethod;
    private Method getIntArrayMethod;
    private Method getCompoundListMethod;
    private Method getCompoundMethod;
    private Method getStringMethod;
    private Method getIntegerMethod;
    private Method getBooleanMethod;
    private Method hasKeyMethod;
    private Method getKeysMethod;
    private Method sizeMethod;
    private Method getMethod;

    public RoomParser() {
        try {
            nbtClass = Class.forName("de.tr7zw.changeme.nbtapi.NBT");
            nbtCompoundClass = Class.forName("de.tr7zw.changeme.nbtapi.NBTCompound");
            nbtCompoundListClass = Class.forName("de.tr7zw.changeme.nbtapi.NBTCompoundList");
            readFileMethod = nbtClass.getMethod("readFile", File.class);
            getIntArrayMethod = nbtCompoundClass.getMethod("getIntArray", String.class);
            getCompoundListMethod = nbtCompoundClass.getMethod("getCompoundList", String.class);
            getCompoundMethod = nbtCompoundClass.getMethod("getCompound", String.class);
            getStringMethod = nbtCompoundClass.getMethod("getString", String.class);
            getIntegerMethod = nbtCompoundClass.getMethod("getInteger", String.class);
            getBooleanMethod = nbtCompoundClass.getMethod("getBoolean", String.class);
            hasKeyMethod = nbtCompoundClass.getMethod("hasKey", String.class);
            getKeysMethod = nbtCompoundClass.getMethod("getKeys");
            sizeMethod = nbtCompoundListClass.getMethod("size");
            getMethod = nbtCompoundListClass.getMethod("get", int.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            log.warn("NBT-API not found on classpath. Structure parsing will be unavailable. Install NBT-API plugin on the server.");
        }
    }

    public boolean isAvailable() {
        return nbtClass != null;
    }

    public ParsedRoom parse(String structureKey, File file) {
        if (!isAvailable()) {
            log.warn("NBT-API not available. Cannot parse structure: {}", structureKey);
            return null;
        }
        if (!file.exists()) {
            log.warn("Structure file not found: {}", file.getAbsolutePath());
            return null;
        }

        try {
            Object nbt = readFileMethod.invoke(null, file);
            if (nbt == null) {
                log.warn("Failed to read NBT data from: {}", file.getAbsolutePath());
                return null;
            }

            int[] size = getSize(nbt);
            if (size == null) {
                log.warn("Missing 'size' tag in structure: {}", structureKey);
                return null;
            }

            List<PaletteEntry> palette = getPalette(nbt);
            if (palette == null) {
                log.warn("Missing 'palette' tag in structure: {}", structureKey);
                return null;
            }

            List<JigsawConnector> connectors = getJigsawConnectors(nbt, palette);
            return new ParsedRoom(structureKey, size[0], size[1], size[2], connectors);

        } catch (Exception e) {
            log.error("Error parsing structure file {}: {}", file.getAbsolutePath(), e.getMessage());
            return null;
        }
    }

    private int[] getSize(Object nbt) {
        try {
            return (int[]) getIntArrayMethod.invoke(nbt, "size");
        } catch (Exception e) {
            log.warn("Failed to read size from NBT: {}", e.getMessage());
            return null;
        }
    }

    private List<PaletteEntry> getPalette(Object nbt) {
        List<PaletteEntry> palette = new ArrayList<>();
        try {
            Object paletteList = getCompoundListMethod.invoke(nbt, "palette");
            if (paletteList == null) return null;
            int size = (int) sizeMethod.invoke(paletteList);
            for (int i = 0; i < size; i++) {
                Object entry = getMethod.invoke(paletteList, i);
                String name = (String) getStringMethod.invoke(entry, "Name");
                Map<String, String> properties = new HashMap<>();
                if ((boolean) hasKeyMethod.invoke(entry, "Properties")) {
                    Object props = getCompoundMethod.invoke(entry, "Properties");
                    for (String key : (String[]) getKeysMethod.invoke(props)) {
                        properties.put(key, (String) getStringMethod.invoke(props, key));
                    }
                }
                palette.add(new PaletteEntry(name, properties));
            }
        } catch (Exception e) {
            log.warn("Failed to read palette from NBT: {}", e.getMessage());
            return null;
        }
        return palette;
    }

    private List<JigsawConnector> getJigsawConnectors(Object nbt, List<PaletteEntry> palette) {
        List<JigsawConnector> connectors = new ArrayList<>();
        try {
            Object blocksList = getCompoundListMethod.invoke(nbt, "blocks");
            if (blocksList == null) return connectors;
            int size = (int) sizeMethod.invoke(blocksList);

            for (int i = 0; i < size; i++) {
                Object block = getMethod.invoke(blocksList, i);
                int stateIndex = (int) getIntegerMethod.invoke(block, "state");

                if (stateIndex < 0 || stateIndex >= palette.size()) continue;

                PaletteEntry paletteEntry = palette.get(stateIndex);
                if (!JIGSAW_BLOCK_ID.equals(paletteEntry.name)) continue;

                int[] pos = (int[]) getIntArrayMethod.invoke(block, "pos");
                if (pos == null || pos.length < 3) continue;

                String orientation = paletteEntry.properties.get("orientation");
                BlockFace facing = BlockFace.UP;
                if (orientation != null) {
                    BlockFace mapped = ORIENTATION_TO_FACE.get(orientation);
                    if (mapped != null) facing = mapped;
                }

                String name = "";
                String target = "";
                String pool = "";
                String joint = "rollable";
                boolean finalState = false;

                if ((boolean) hasKeyMethod.invoke(block, "nbt")) {
                    Object blockNbt = getCompoundMethod.invoke(block, "nbt");
                    if ((boolean) hasKeyMethod.invoke(blockNbt, "name")) name = (String) getStringMethod.invoke(blockNbt, "name");
                    if ((boolean) hasKeyMethod.invoke(blockNbt, "target")) target = (String) getStringMethod.invoke(blockNbt, "target");
                    if ((boolean) hasKeyMethod.invoke(blockNbt, "pool")) pool = (String) getStringMethod.invoke(blockNbt, "pool");
                    if ((boolean) hasKeyMethod.invoke(blockNbt, "joint")) joint = (String) getStringMethod.invoke(blockNbt, "joint");
                    if ((boolean) hasKeyMethod.invoke(blockNbt, "final_state")) finalState = (boolean) getBooleanMethod.invoke(blockNbt, "final_state");
                }

                connectors.add(new JigsawConnector(
                        pos[0], pos[1], pos[2],
                        facing,
                        name, target, pool, joint, finalState
                ));
            }
        } catch (Exception e) {
            log.warn("Failed to read blocks from NBT: {}", e.getMessage());
        }
        return connectors;
    }

    private static class PaletteEntry {
        final String name;
        final Map<String, String> properties;

        PaletteEntry(String name, Map<String, String> properties) {
            this.name = name;
            this.properties = properties;
        }
    }
}
