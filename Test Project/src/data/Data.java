package data;

import beans.UserEntity;

public class Data {
    private static UserEntity user;
    private static Thread notifierThread;

    public static UserEntity getUser() {
        return user;
    }

    public static void setUser(UserEntity userEntity) {
        Data.user = userEntity;
    }

    public static Thread getNotifierThread() {
        return notifierThread;
    }

    public static void setNotifierThread(Thread notifierThread) {
        Data.notifierThread = notifierThread;
    }
}
