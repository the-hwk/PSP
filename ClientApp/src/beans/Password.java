package beans;

public class Password {
    private final int id;
    private final User user;
    private final byte[] password;

    public Password(int id, User user, byte[] password) {
        this.id = id;
        this.user = user;
        this.password = password;
    }
}
