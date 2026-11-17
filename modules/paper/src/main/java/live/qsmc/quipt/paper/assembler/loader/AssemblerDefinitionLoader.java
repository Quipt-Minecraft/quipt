package live.qsmc.quipt.paper.assembler.loader;

import live.qsmc.quipt.paper.assembler.definition.AssemblerDefinition;
import live.qsmc.quipt.paper.assembler.definition.Pool;
import live.qsmc.quipt.paper.assembler.definition.RoomEntry;
import live.qsmc.quipt.paper.assembler.model.ParsedRoom;
import live.qsmc.quipt.paper.assembler.parser.RoomParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Scans datapack directories and plugin resources for assembler definitions
 * and structure rooms, then caches the parsed results.
 */
public class AssemblerDefinitionLoader {

    private static final Logger log = LoggerFactory.getLogger(AssemblerDefinitionLoader.class);

    private final RoomParser roomParser;
    private final Map<String, ParsedRoom> roomCache;
    private final Map<String, AssemblerDefinition> definitionCache;
    private File dataFolder;

    public AssemblerDefinitionLoader() {
        this.roomParser = new RoomParser();
        this.roomCache = new HashMap<>();
        this.definitionCache = new HashMap<>();
    }

    /**
     * Set the data folder where datapacks are located.
     */
    public void setDataFolder(File dataFolder) {
        this.dataFolder = dataFolder;
    }

    /**
     * Reload all definitions and rooms from disk and plugin resources.
     */
    public void reload() {
        roomCache.clear();
        definitionCache.clear();

        // Scan plugin JAR resources first
        scanResourceDirectory("/data");

        // Scan world datapack folders
        if (dataFolder != null) {
            scanDatapackFolders();
        }

        log.info("Loaded {} assembler definitions and {} rooms",
                definitionCache.size(), roomCache.size());
    }

    /**
     * Scan plugin JAR resources for definitions and rooms.
     */
    private void scanResourceDirectory(String basePath) {
        // Scan for assembler definitions
        scanResourcePath(basePath, "quipt_assembler", ".json", this::parseDefinitionFile);

        // Scan for structure rooms
        scanResourcePath(basePath, "structure", ".nbt", this::parseStructureFile);
    }

    /**
     * Scan a resource path for files matching the given extension.
     */
    private void scanResourcePath(String basePath, String subDir, String extension,
                                  ResourceFileHandler handler) {
        try (InputStream is = getClass().getResourceAsStream(basePath + "/" + subDir)) {
            if (is == null) {
                return; // No such resource directory
            }
            // This approach won't work for JAR resources directly;
            // we'll rely on the datapack folder scanning instead
        } catch (IOException e) {
            log.warn("Failed to scan resource directory: {}", basePath + "/" + subDir, e);
        }
    }

    /**
     * Scan world datapack folders for definitions and rooms.
     */
    private void scanDatapackFolders() {
        if (dataFolder == null || !dataFolder.exists()) {
            return;
        }

        // Scan datapacks directory
        File datapacksDir = new File(dataFolder, "datapacks");
        if (datapacksDir.exists()) {
            for (File pack : datapacksDir.listFiles()) {
                if (pack.isDirectory()) {
                    scanPackData(pack);
                }
            }
        }

        // Also scan the world's data directory directly
        File worldDataDir = new File(dataFolder, "data");
        if (worldDataDir.exists()) {
            scanDataDirectory(worldDataDir);
        }
    }

    /**
     * Scan a datapack's data directory.
     */
    private void scanPackData(File pack) {
        File dataDir = new File(pack, "data");
        if (dataDir.exists()) {
            scanDataDirectory(dataDir);
        }
    }

    /**
     * Scan a data directory for all namespaces.
     */
    private void scanDataDirectory(File dataDir) {
        File[] namespaces = dataDir.listFiles();
        if (namespaces == null) {
            return;
        }

        for (File ns : namespaces) {
            if (!ns.isDirectory()) {
                continue;
            }

            String namespace = ns.getName();

            // Scan for assembler definitions
            File assemblerDir = new File(ns, "quipt_assembler");
            if (assemblerDir.isDirectory()) {
                File[] jsonFiles = assemblerDir.listFiles((dir, name) -> name.endsWith(".json"));
                if (jsonFiles != null) {
                    for (File jsonFile : jsonFiles) {
                        parseDefinitionFile(namespace, jsonFile);
                    }
                }
            }

            // Scan for structure rooms
            File structureDir = new File(ns, "structure");
            if (structureDir.isDirectory()) {
                File[] nbtFiles = structureDir.listFiles((dir, name) -> name.endsWith(".nbt"));
                if (nbtFiles != null) {
                    for (File nbtFile : nbtFiles) {
                        parseStructureFile(namespace, nbtFile);
                    }
                }
            }
        }
    }

    /**
     * Parse an assembler definition JSON file.
     */
    private void parseDefinitionFile(String namespace, File file) {
        String id = file.getName().replace(".json", "");
        String fullId = namespace + ":" + id;

        try (InputStream is = new FileInputStream(file)) {
            byte[] bytes = is.readAllBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            JSONObject json = new JSONObject(content);

            String startRoom = json.optString("start", "");
            int budget = json.optInt("budget", 100);
            int maxDepth = json.optInt("max_depth", 12);
            int maxPieces = json.optInt("max_pieces", 64);

            Map<String, RoomEntry> rooms = new HashMap<>();
            if (json.has("rooms")) {
                JSONObject roomsJson = json.getJSONObject("rooms");
                for (String key : roomsJson.keySet()) {
                    JSONObject roomJson = roomsJson.getJSONObject(key);
                    int cost = roomJson.optInt("cost", 1);
                    int weight = roomJson.optInt("weight", 1);
                    boolean terminator = roomJson.optBoolean("terminator", false);
                    rooms.put(key, new RoomEntry(cost, weight, terminator));
                }
            }

            Map<String, Pool> pools = new HashMap<>();
            if (json.has("pools")) {
                JSONObject poolsJson = json.getJSONObject("pools");
                for (String key : poolsJson.keySet()) {
                    JSONArray poolArray = poolsJson.getJSONArray(key);
                    List<String> roomKeys = new ArrayList<>();
                    for (int i = 0; i < poolArray.length(); i++) {
                        roomKeys.add(poolArray.getString(i));
                    }
                    pools.put(key, new Pool(key, roomKeys));
                }
            }

            AssemblerDefinition definition = new AssemblerDefinition(
                    fullId, startRoom, budget, maxDepth, maxPieces, rooms, pools
            );
            definitionCache.put(fullId, definition);
            log.debug("Loaded assembler definition: {} from {}", fullId, file.getAbsolutePath());

        } catch (Exception e) {
            log.error("Failed to parse assembler definition {}: {}", fullId, e.getMessage());
        }
    }

    /**
     * Parse a structure .nbt file.
     */
    private void parseStructureFile(String namespace, File file) {
        String structureName = file.getName().replace(".nbt", "");
        String structureKey = namespace + ":" + structureName;

        ParsedRoom room = roomParser.parse(structureKey, file);
        if (room != null) {
            roomCache.put(structureKey, room);
            log.debug("Loaded room: {} from {} ({} connectors)",
                    structureKey, file.getAbsolutePath(), room.connectors().size());
        }
    }

    /**
     * Get a cached room by key.
     */
    public ParsedRoom getRoom(String key) {
        return roomCache.get(key);
    }

    /**
     * Get a cached definition by ID.
     */
    public AssemblerDefinition getDefinition(String id) {
        return definitionCache.get(id);
    }

    /**
     * Get all cached rooms.
     */
    public Map<String, ParsedRoom> getRooms() {
        return Collections.unmodifiableMap(roomCache);
    }

    /**
     * Get all cached definitions.
     */
    public Map<String, AssemblerDefinition> getDefinitions() {
        return Collections.unmodifiableMap(definitionCache);
    }

    /**
     * Functional interface for handling resource files.
     */
    @FunctionalInterface
    private interface ResourceFileHandler {
        void handle(String namespace, File file);
    }
}
