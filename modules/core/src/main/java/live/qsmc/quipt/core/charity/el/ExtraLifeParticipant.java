package live.qsmc.quipt.core.charity.el;

import live.qsmc.quipt.core.Quipt;
import live.qsmc.quipt.core.QuiptIntegration;
import live.qsmc.quipt.core.config.files.ExtraLifeConfig;
import live.qsmc.quipt.core.config.objects.ConfigObject;
import live.qsmc.quipt.core.utils.net.NetworkUtils;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.net.http.HttpResponse;


public class ExtraLifeParticipant extends ConfigObject {

    private final LifeManager lifeManager = new LifeManager();
    public String team;
    public boolean boogey;
    public int lives;
    public long donorDriveId;
    private JSONArray incentives_cache = null;
    private long last_incentives_fetch = 0;

    public ExtraLifeParticipant(QuiptIntegration integration) {
        super(integration);
    }

    public ExtraLifeParticipant(QuiptIntegration integration, String id, String team, int lives, long donorDriveId) {
        super(integration);
        this.id = id;
        this.team = team;
        this.lives = lives;
        this.boogey = false;
        this.donorDriveId = donorDriveId;
    }

    public JSONArray getIncentives() {
        return getIncentives(false);
    }

    public JSONArray getIncentives(boolean force) {
        long now = System.currentTimeMillis();

        if (force || now - last_incentives_fetch >= 60 * 1000L) {
            HttpResponse<String> response = null;
            try {
                response = NetworkUtils.get(NetworkUtils.DEFAULT, Quipt.INSTANCE.configs().config(ExtraLifeConfig.class).api_endpoint + "participants/" + this.donorDriveId + "/incentives");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            incentives_cache = new JSONArray(response.body());
            last_incentives_fetch = now;

        }

        return incentives_cache;
    }

    public LifeManager lives() {
        return lifeManager;
    }


    public class LifeManager {

        public int add() {
            return edit(1);
        }

        public int add(int amount) {
            return edit(amount);
        }

        public int remove() {
            return edit(-1);
        }

        public int remove(int amount) {
            return edit(-amount);
        }

        public int edit(int amount) {
            int lives = ExtraLifeParticipant.this.lives + amount;
            return set(lives);
        }

        public int set(int lives) {
            ExtraLifeParticipant.this.lives = lives;
            return lives();
        }

        public int lives() {
            return ExtraLifeParticipant.this.lives;
        }

        public int get() {
            return ExtraLifeParticipant.this.lives;
        }
    }


}
