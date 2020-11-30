package beans;

public class User {
    private final int id;
    private final String email;
    private final String nickname;
    private final Role role;

    public User(int id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = name;
        this.role = role;
    }

    enum Role {
        ADMIN,
        USER,
        GUEST
    }
}
