package server.models;

import server.GsonContainer;
import server.enums.Action;
import server.enums.Status;

import java.util.HashMap;
import java.util.Map;

public class UDPMessage {
    private final Action action;
    private final Map<String, Object> properties;
    private final Status status;

    public UDPMessage(Action action, Status status) {
        this(action, new HashMap<String, Object>(), status);
    }

    public UDPMessage(Action action, Map<String, Object> properties, Status status) {
        this.action = action;
        this.properties = properties;
        this.status = status;
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