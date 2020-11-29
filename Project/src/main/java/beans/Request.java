package beans;

import com.google.gson.Gson;
import java.util.Map;

public class Request {
    private Action action;
    private Map<String, Param> params;

    private static final Gson gson = new Gson();

    public static Request parse(String request) {
        return gson.fromJson(request, Request.class);
    }

    public Action getAction() {
        return action;
    }

    public Map<String, Param> getParams() {
        return params;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

    enum Action {
        CONNECTION,
        ROOM,
        MESSAGE
    }
}