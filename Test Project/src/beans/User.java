package beans;

public class User {
    private final int id;
    private String email;
    private String nickname;
    private Role role;

    public User() {
        this(-1);
    }

    public User(int id) {
        this.id = id;
    }

    public User(String email) {
        this();
        this.email = email;
    }

    public User(String email, String nickname) {
        this();
        this.email = email;
        this.nickname = nickname;
    }

    public User(int id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    enum Role {
        ADMIN,
        USER
    }
}
