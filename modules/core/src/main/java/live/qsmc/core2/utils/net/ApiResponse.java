package live.qsmc.core2.utils.net;

import live.qsmc.core2.data.JsonSerializable;
import org.json.JSONObject;

import java.net.http.HttpResponse;

public class ApiResponse<T> implements JsonSerializable {

    public Status status;
    public T data;

    public ApiResponse(HttpResponse<?> response) {
        if(response == null)
            throw new IllegalArgumentException("Response cannot be null");
        if(response.body() == null)
            throw new IllegalArgumentException("Response body cannot be null");
        JSONObject json;
        if(response.body() instanceof JSONObject bodyJson)
            json = bodyJson;
        else
            json = new JSONObject(response.body() + "");
        status = Status.valueOf(json.getString("status").toUpperCase());
        if(json.has("data"))
            data = (T) json.get("data");
        else data = null;
    }

    public ApiResponse(Status status, T data) {
        this.status = status;
        this.data = data;
    }

    public Status status(){
        return status;
    }

    public T data(){
        return data;
    }


    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isFailure() {
        return status == Status.FAILURE;
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
