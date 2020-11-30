package beans;

public class Message {
    private final int id;
    private final String value;
    private final User from;
    private final Room to;

    public Message(int id, String value, User from, Room to) {
        this.id = id;
        this.value = value;
        this.from = from;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public User getFrom() {
        return from;
    }

    public Room getTo() {
        return to;
    }
}