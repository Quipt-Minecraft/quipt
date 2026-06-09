package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.charity.tiltify.TiltifyManager;
import live.qsmc.quipt.core.events.charity.DonationUpdateEvent;
import live.qsmc.quipt.core.utils.TestUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GuildRushTest {

    private static QuiptIntegration integration;
    private static Quipt quipt;

    @BeforeAll
    public static void setup(){
        integration = TestUtils.getTestIntegration();
        quipt = Quipt.INSTANCE;
        quipt.enable(integration);
    }

    @Test
    public void detectDonations(){
        quipt.events().register(new DonationEventListener(DonationUpdateEvent.class));

        TiltifyManager tiltify = Quipt.INSTANCE.charities().tiltify(integration);
        tiltify.update();

    }
}
