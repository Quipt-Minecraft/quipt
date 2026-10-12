package live.qsmc.quipt.core.events;

public class CancellableEvent<T extends EventData> extends Event<T> implements Event.Cancellable {

    private boolean cancelled = false;

    public CancellableEvent(T original) {
        super(original);
    }

    /**
     * Cancel or uncancel the event.
     * @param cancel true to cancel the event, false to uncancel it
     */
    public void cancel(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean cancelled() {
        return cancelled;
    }
}
