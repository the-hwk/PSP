package beans;

import com.google.gson.Gson;

public class GsonContainer {
    private static final Gson GSON = new Gson();

    public static Gson getGson() {
        return GSON;
    }
}
