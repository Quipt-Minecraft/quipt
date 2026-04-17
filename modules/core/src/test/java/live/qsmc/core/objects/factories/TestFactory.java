package live.qsmc.core.objects.factories;

import live.qsmc.core.config.ConfigObject;
import live.qsmc.core.objects.JsonTest;
import org.json.JSONObject;

public class TestFactory implements ConfigObject.Factory<JsonTest> {
    @Override
    public String getClassName() {
        return JsonTest.class.getName();
    }

    @Override
    public JsonTest createFromJson(JSONObject json) {
        JsonTest participant = new JsonTest();
        participant.fromJson(json);
        return participant;
    }
}