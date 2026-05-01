package live.qsmc.core2.utils.net;

import live.qsmc.core2.data.JsonSerializable;
import org.json.JSONObject;

public record ApiResponse<T>(Status status, T data) implements JsonSerializable {

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isFailure() {
        return status == Status.FAILURE;
    }

    @Override
    public JSONObject json() {
        return new JSONObject()
                .put("status", status.name())
                .put("data", data);
    }

    public enum Status {
        SUCCESS,
        FAILURE,
        WARNING,
        NO_ACTION,
        NO_RESPONSE,
        NO_PERMISSION,
        NO_ACCOUNT,
        NO_TOKEN,
        NO_DATA,
        NO_PERMISSION_DATA,
        NO_ACCOUNT_DATA,
        NO_TOKEN_DATA,
        NO_STORAGE,
        NO_PERMISSION_STORAGE,
        NO_ACCOUNT_STORAGE,
        NO_TOKEN_STORAGE,
    }

    public static class JsonResponse implements JsonSerializable {

        public JSONObject data = new JSONObject();
    }
}
