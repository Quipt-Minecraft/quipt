package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public abstract class CharityPlatform<O extends CharityDonation> {

    public final String id;
    public final String name;
    private final Class<O> donationClass;
    private final QuiptIntegration integration;

    public CharityPlatform(QuiptIntegration integration, String id, String name, Class<O> donationClass){
        this.integration = integration;
        this.id = id;
        this.name = name;
        this.donationClass = donationClass;
    }

    public abstract ProcessResult<O> process(O donation);

    public String name(){
        return name;
    }

    public String id(){
        return id;
    }

    public O donation(JSONObject raw){
        try {
            O o = donationClass.getConstructor().newInstance();
            o.fromJson(raw);
            return o;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
