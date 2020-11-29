package beans;

import annotations.DbField;
import annotations.DbName;

@DbName("messages")
public class Message {
    @DbField("id")
    private final int id;
    @DbField("value")
    private final String value;
    @DbField("from")
    private final User from;
    @DbField("to")
    private final Room to;

    public Message(int id, String value, User from, Room to) {
        this.id = id;
        this.value = value;
        this.from = from;
        this.to = to;
    }
}