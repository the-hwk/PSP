package db.dao;

import db.dao.interfaces.MessageDao;
import db.dao.interfaces.PasswordDao;
import db.dao.interfaces.RoomDao;
import db.dao.interfaces.UserDao;
import db.dao.mysql.MySqlDaoFactory;

public abstract class DaoFactory {
    public abstract MessageDao getMessageDao();
    public abstract PasswordDao getPasswordDao();
    public abstract RoomDao getRoomDao();
    public abstract UserDao getUserDao();

    public static DaoFactory getDaoFactory(Consumer consumer) {
        if (consumer == Consumer.MYSQL) {
            return new MySqlDaoFactory();
        }
        throw new IllegalArgumentException();
    }

    public enum Consumer {
        MYSQL
    }
}