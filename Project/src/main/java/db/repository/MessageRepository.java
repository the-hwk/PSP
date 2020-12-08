package db.repository;

import entities.MessageEntity;
import entities.RoomEntity;

import java.util.List;

public interface MessageRepository extends Repository<MessageEntity> {
    List<MessageEntity> findForRoom(RoomEntity room);
}
