package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.el.ExtraLifePlatform;
import live.qsmc.quipt.core.charity.tiltify.TiltifyPlatform;
import live.qsmc.quipt.core.charity.tiltify.old.TiltifyManager2;
import live.qsmc.quipt.core.data.registries.Registry;

import java.util.Optional;
import java.util.function.Function;

public class CharityManager {

    private final Registry<CharityInitializer> platforms;

    public CharityManager() {
        platforms = Quipt.INSTANCE.registries().register("charity_platforms", () -> null);
        platforms.register("tiltify", TiltifyPlatform::new);
        platforms.register("extralife", ExtraLifePlatform::new);
    }

    public final <T extends CharityPlatform<?>> T initialize(QuiptIntegration integration, String platformName, Class<T> platformClass) {
        Optional<CharityInitializer> platform = platforms.get(platformName);
        return platform.map(charityInitializer -> platformClass.cast(charityInitializer.apply(integration))).orElse(null);
    }

    public interface CharityInitializer extends Function<QuiptIntegration, CharityPlatform<?>> {

    }

}
