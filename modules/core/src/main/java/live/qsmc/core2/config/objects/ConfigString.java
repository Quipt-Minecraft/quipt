package live.qsmc.core2.config.objects;

import live.qsmc.core2.QuiptIntegration;
import org.json.JSONObject;

public class ConfigString extends ConfigObject {

    String value;
    public ConfigString(QuiptIntegration integration, String id, String value){
        super(integration);
        this.value = value;
        super.id = id;
    }

    public ConfigString(QuiptIntegration integration, JSONObject json){
        super(integration, json);
    }

    public String value(){
        return value;
    }

    @Override
    public String toString() {

        return value();
    }
}