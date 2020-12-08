package db.repository.impl;

import entities.RoomEntity;
import entities.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RepositoryImplTest {
    @Test
    void findById() {
        UserEntity test = new UserEntity();
        test.setId(1);
        test.setEmail("test@mail.ru");
        test.setNickname("Test");
        test.setRoleId(1);

        UserEntity user = RepositoryFactory.getUserRepository().findById(1);

        Assertions.assertEquals(test, user);
    }

    @Test
    void findAll() {
    }

    @Test
    void save() {
        UserEntity user = RepositoryFactory.getUserRepository().findById(1);

        RoomEntity room = new RoomEntity();
        room.setName("Test room");
        room.addUser(user);

        RepositoryFactory.getRoomRepository().save(room);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}