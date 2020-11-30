package beans;

import java.util.HashMap;
import java.util.Map;

public class UDPMessage {
    private final Action action;
    private final Map<String, Object> properties;
    private final Status status;

    public UDPMessage(Action action) {
        this(action, new HashMap<String, Object>());
    }

    public UDPMessage(Action action, Map<String, Object> properties) {
        this.action = action;
        this.properties = properties;
        this.status = null;
    }

    public Action getAction() {
        return action;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return GsonContainer.getGson().toJson(this);
    }
}