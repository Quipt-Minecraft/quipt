package live.qsmc.quipt.core.charity.event;

import live.qsmc.quipt.core.charity.CharityDonation;
import live.qsmc.quipt.core.charity.CharityPlatform;
import live.qsmc.quipt.core.events.EventListener;

public abstract class DonationUpdateListener<P extends CharityPlatform<D,?>, D extends CharityDonation> extends EventListener<DonationUpdateEvent<P, D>, DonationUpdateEvent.Data<D>> {
}
