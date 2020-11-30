package db.dao.interfaces;

import beans.Message;
import beans.Room;

import java.util.List;

public interface MessageDao extends CrudOperations<Message> {
    List<Message> selectByRoom(Room room);
}
