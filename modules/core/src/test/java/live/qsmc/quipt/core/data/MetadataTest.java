package live.qsmc.quipt.core.data;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataTest {

    @Test
    public void testNestedMetadataSerialization() {
        JSONObject inner = new JSONObject();
        inner.put("boogey_boss_bar", false);
        inner.put("boogey_particles", true);

        JSONObject root = new JSONObject();
        root.put("settings", inner);

        Metadata metadata = Metadata.of(root);
        assertNotNull(metadata.get("settings"));

        JSONObject exported = metadata.json();
        Object settingsObj = exported.get("settings");
        assertTrue(settingsObj instanceof JSONObject, "Nested metadata should be serialized as JSONObject, not String");

        JSONObject settingsJson = (JSONObject) settingsObj;
        assertFalse(settingsJson.getBoolean("boogey_boss_bar"));
        assertTrue(settingsJson.getBoolean("boogey_particles"));
    }
}
