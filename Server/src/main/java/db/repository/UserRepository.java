package db.repository;

import entities.RoomEntity;
import entities.UserEntity;
import org.hibernate.Session;

public interface UserRepository extends Repository<UserEntity> {
    UserEntity findByEmail(String email);
    void save(Session session, RoomEntity room);
}
