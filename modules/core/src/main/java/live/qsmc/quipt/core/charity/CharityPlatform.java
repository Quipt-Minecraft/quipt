package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.Config;
import live.qsmc.quipt.core.config.files.ExtraLifeConfig;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;

public abstract class CharityPlatform<O extends CharityDonation, C extends Config> {

    public final String id;
    public final String name;
    private final Class<O> donationClass;
    private final QuiptIntegration integration;
    private final C config;

    public CharityPlatform(QuiptIntegration integration, String id, String name, Class<O> donationClass, Class<C> configClass){
        this.integration = integration;
        this.id = id;
        this.name = name;
        this.donationClass = donationClass;
        config = integration().configs().register(configClass);
    }

    public final C config(){
        return config;
    }

    public QuiptIntegration integration(){
        return integration;
    }

    public abstract ProcessResult<O> process(O donation);

    public String name(){
        return name;
    }

    public String id(){
        return id;
    }

    public O donation(JSONObject raw, String id){
        try {
            O o = donationClass.getConstructor(QuiptIntegration.class).newInstance(integration);
            o.fromJson(raw);
            o.id = id;
            return o;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

    }
}
