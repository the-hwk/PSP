package db.dao.interfaces;

import beans.Room;
import beans.User;

import java.util.List;

public interface RoomDao extends CrudOperations<Room> {
    List<Room> selectByUser(User user);
}
