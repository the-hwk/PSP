package server.models;

import server.GsonContainer;
import server.enums.Action;
import server.enums.Status;

import java.net.InetAddress;
import java.net.SocketAddress;

public class UDPMessage {
    private final Action action;
    private final String body;
    private final Status status;
    private SocketAddress socket;

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

    public SocketAddress getSocket() {
        return socket;
    }

    public void setSocket(SocketAddress socket) {
        this.socket = socket;
    }

    @Override
    public String toString() {
        return GsonContainer.getGson().toJson(this);
    }
}