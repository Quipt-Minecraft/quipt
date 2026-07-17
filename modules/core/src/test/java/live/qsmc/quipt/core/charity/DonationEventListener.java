package live.qsmc.quipt.core.charity;

import live.qsmc.quipt.core.charity.event.DonationUpdateEvent;
import live.qsmc.quipt.core.events.EventListener;

import java.util.List;

public class DonationEventListener<P extends CharityPlatform<D>, D extends CharityDonation> extends EventListener<DonationUpdateEvent<P, D>, DonationUpdateEvent.Data<D>> {

    public DonationEventListener(Class<DonationUpdateEvent<P, D>> clazz) {
        super(clazz);
    }

    @Override
    public void handle(DonationUpdateEvent<P, D> event) {
        List<ProcessResult<D>> donations = event.data().donations();
        for (ProcessResult<D> result : donations) {
            if (!result.type().equals(ProcessResult.Type.SUCCESS)) continue;
            D donation = result.donation();
            donation.amount();
            System.out.println("New " + donation.amount() + " donation from " + donation.donor() + " to " + donation.participant() + " detected!");
            System.out.println("New Donation Detected: \n" + donation.json().toString(2));
        }

    }
}



