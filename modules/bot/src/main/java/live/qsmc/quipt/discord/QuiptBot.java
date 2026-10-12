package live.qsmc.quipt.discord;

import live.qsmc.quipt.core.QuiptIntegration;

public class QuiptBot extends BotModule {

    private static QuiptBot instance;

    public static QuiptBot instance() {
        return instance;
    }

    @Override
    public void enable() {
        super.enable();
        instance = this;
    }

    @Override
    public String name() {
        return "QuiptBot";
    }

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public String id() {
        return "quiptbot";
    }
}
