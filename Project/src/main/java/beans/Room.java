package beans;

import java.util.List;

public class Room {
    private final int id;
    private final String name;
    private final List<User> users;

    public Room(int id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }
}
