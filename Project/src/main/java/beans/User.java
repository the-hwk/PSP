package beans;

import java.util.Optional;

public class User {
    private final int id;
    private String email;
    private String nickname;
    private Role role;

    public User(int id) {
        this.id = id;
    }

    public User(int id, String email, String name, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = name;
        this.role = role;
    }

    public User(int id, User user) {
        this(id, user.getEmail(), user.getNickname(), user.getRole());
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public Role getRole() {
        return role;
    }

    public enum Role {
        ADMIN(1),
        USER(0);

        private final int id;

        Role(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Optional<Role> fromInt(int value) {
            for (Role role : values()) {
                if (role.id == value) {
                    return Optional.of(role);
                }
            } return Optional.empty();
        }
    }
}
