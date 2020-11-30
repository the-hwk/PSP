package beans;

import java.util.List;

public class Room {
    private final int id;
    private String name;
    private List<User> users;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Room(int id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getUsers() {
        return users;
    }
}
