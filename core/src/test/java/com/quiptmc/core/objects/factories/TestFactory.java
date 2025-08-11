package com.quiptmc.core.objects.factories;

import com.quiptmc.core.config.ConfigObject;
import com.quiptmc.core.objects.JsonTest;
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