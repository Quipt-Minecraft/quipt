package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.tiltify.TiltifyManager;
import live.qsmc.quipt.core.data.registries.Registry;

import java.util.function.Function;

public class CharityManager {

    private final Registry<Function<QuiptIntegration, CharityPlatform>> platforms;

    public CharityManager(){
        platforms = Quipt.INSTANCE.registries().register("charity_platforms", ()->null);
        platforms.register("tiltify", TiltifyManager::new);
    }

    public final TiltifyManager tiltify(QuiptIntegration integration) {
        return (TiltifyManager) platforms.get("tiltify").get().apply(integration);
    }

}
