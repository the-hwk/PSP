package db.repository.impl;

import db.repository.RoomRepository;
import entities.RoomEntity;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomRepositoryImplTest {
    RoomRepository repository = RepositoryFactory.getRoomRepository();
    static RoomEntity testRoom = new RoomEntity();

    @BeforeAll
    static void initTestUserEntity() {
        testRoom.setName("test_room_0");
    }

    @Test
    @Order(1)
    void save() {
        Assertions.assertDoesNotThrow(() -> repository.save(testRoom));
    }

    @Test
    @Order(2)
    void saveWithNullValues() {
        RoomEntity room = new RoomEntity();
        room.setName(null);

        Assertions.assertThrows(PropertyValueException.class, () -> repository.save(room));
    }

    @Test
    @Order(3)
    void findById() {
        RoomEntity actual = repository.findById(testRoom.getId());

        Assertions.assertEquals(testRoom, actual);
    }

    @Test
    @Order(4)
    void update() {
        RoomEntity room = repository.findById(testRoom.getId());
        room.setName("updated_test_room_0");

        Assertions.assertDoesNotThrow(() -> repository.update(room));
    }

    @Test
    @Order(5)
    void updateWithNullValues() {
        RoomEntity room = repository.findById(testRoom.getId());
        room.setName(null);

        Assertions.assertThrows(PersistenceException.class, () -> repository.update(room));
    }

    @Test
    @Order(6)
    void delete() {
        RoomEntity room = repository.findById(testRoom.getId());

        Assertions.assertDoesNotThrow(() -> repository.delete(room));
    }
}