package live.qsmc.quipt.core.charity;


public record ProcessResult<D extends CharityDonation>(Type type, D donation) {
    public enum Type {
        SUCCESS,
        FAILURE,
        INVALID;
    }
}
