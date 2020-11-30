package info;

import beans.User;

public class Data {
    private static User user;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Data.user = user;
    }
}
