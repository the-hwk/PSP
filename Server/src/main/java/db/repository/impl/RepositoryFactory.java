package db.repository.impl;

import db.repository.MessageRepository;
import db.repository.PasswordRepository;
import db.repository.RoomRepository;
import db.repository.UserRepository;

public class RepositoryFactory {
    public static MessageRepository getMessageRepository() {
        return MessageRepositoryImpl.getInstance();
    }

    public static PasswordRepository getPasswordRepository() {
        return PasswordRepositoryImpl.getInstance();
    }

    public static RoomRepository getRoomRepository() {
        return RoomRepositoryImpl.getInstance();
    }

    public static UserRepository getUserRepository() {
        return UserRepositoryImpl.getInstance();
    }
}
