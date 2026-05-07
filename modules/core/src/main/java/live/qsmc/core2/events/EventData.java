package live.qsmc.core2.events;

public interface EventData<R> {

    R data();

    Class<R> dataClass();

    default boolean isDataNull() {
        return data() == null;
    }
}
