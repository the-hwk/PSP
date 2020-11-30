package db.dao.mysql;

import db.dao.DaoFactory;
import db.dao.interfaces.MessageDao;
import db.dao.interfaces.PasswordDao;
import db.dao.interfaces.RoomDao;
import db.dao.interfaces.UserDao;

public class MySqlDaoFactory extends DaoFactory {
    private MessageDao messageDao;
    private PasswordDao passwordDao;
    private RoomDao roomDao;
    private UserDao userDao;


    @Override
    public MessageDao getMessageDao() {
        return messageDao == null ? new MySqlMessageDao() : messageDao;
    }

    @Override
    public PasswordDao getPasswordDao() {
        return passwordDao == null ? new MySqlPasswordDao() : passwordDao;
    }

    @Override
    public RoomDao getRoomDao() {
        return roomDao == null ? new MySqlRoomDao() : roomDao;
    }

    @Override
    public UserDao getUserDao() {
        return userDao == null ? new MySqlUserDao() : userDao;
    }
}