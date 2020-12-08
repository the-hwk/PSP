package beans;

public class UDPMessage {
    private final Action action;
    private final String body;
    private final Status status;

    public UDPMessage(Action action, String body) {
        this.action = action;
        this.body = body;
        this.status = null;
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

    @Override
    public String toString() {
        return GsonContainer.getGson().toJson(this);
    }
}