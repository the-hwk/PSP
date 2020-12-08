package info;

import beans.UserEntity;

public class Data {
    private static UserEntity userEntity;

    public static UserEntity getUser() {
        return userEntity;
    }

    public static void setUser(UserEntity userEntity) {
        Data.userEntity = userEntity;
    }
}
