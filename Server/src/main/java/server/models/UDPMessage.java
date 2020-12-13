package server.models;

import containers.GsonContainer;
import server.enums.Action;
import server.enums.Status;

public class UDPMessage {
    private final Action action;
    private final String body;
    private final Status status;
    private String host;

    public UDPMessage(Action action, String body, Status status) {
        this.action = action;
        this.body = body;
        this.status = status;
    }

    public Action getAction() {
        return action;
    }

    public String getBody() {
        return body;
    }

    public Status getStatus() {
        return status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String toString() {
        return GsonContainer.getGson().toJson(this);
    }
}