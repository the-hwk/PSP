package beans;

public class Password {
    private final int id;
    private User user;
    private byte[] password;

    public Password() {
        this(-1);
    }

    public Password(byte[] password) {
        this();
        this.password = password;
    }

    public Password(int id) {
        this.id = id;
    }

    public Password(int id, User user, byte[] password) {
        this.id = id;
        this.user = user;
        this.password = password;
    }
}
