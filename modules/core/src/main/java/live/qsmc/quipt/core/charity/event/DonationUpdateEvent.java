package live.qsmc.quipt.core.charity.event;

import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.charity.ProcessResult;
import live.qsmc.quipt.core.charity.el.ExtraLifeDonation;
import live.qsmc.quipt.core.events.Event;
import live.qsmc.quipt.core.events.EventData;

import java.util.List;

public class DonationUpdateEvent<P extends CharityPlatform<D>, D extends CharityDonation> extends Event<DonationUpdateEvent.Data<D>> {

    P platform;

    public DonationUpdateEvent(P platform, Data<D> original) {
        super(original);
        this.platform = platform;
    }

    public P platform() {
        return platform;
    }

    public static class Data<D extends CharityDonation> extends EventData {

        private final List<ProcessResult<D>> donations;

        public Data(List<ProcessResult<D>> donations){
            this.donations = donations;
        }

        public List<ProcessResult<D>> donations() {
            return donations;
        }

    }
}
