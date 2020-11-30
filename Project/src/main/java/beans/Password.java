package beans;

public class Password {
    private final int id;
    private User user;
    private byte[] value;

    public Password(int id, User user, byte[] password) {
        this.id = id;
        this.user = user;
        this.value = password;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public byte[] getValue() {
        return value;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
