package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.events.EventListener;
import live.qsmc.quipt.core.events.charity.DonationUpdateEvent;

public class DonationEventListener extends EventListener<DonationUpdateEvent, DonationUpdateEvent.Data> {
    public DonationEventListener(Class<DonationUpdateEvent> clazz) {
        super(clazz);
    }

    @Override
    public void handle(DonationUpdateEvent event) {
        System.out.println("New " + event.data().amount() + " donation from " + event.data().donor() + " to " + event.data().participant() + " detected!");
        System.out.println("New Donation Detected: \n" +event.data().raw().toString(2));
    }
}
