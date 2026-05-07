package live.qsmc.quipt.core.utils;

import live.qsmc.quipt.core.QuiptIntegration;

import java.io.File;

public class TestUtils {

    public static QuiptIntegration getTestIntegration() {
        return new QuiptIntegration() {

            @Override
            public String name() {
                return "quipt-test";
            }

            @Override
            public String version() {
                return "in-dev";
            }

            @Override
            public File folder() {
                return new File(name());
            }

            @Override
            public void enable() {

            }
        };
    }
}
