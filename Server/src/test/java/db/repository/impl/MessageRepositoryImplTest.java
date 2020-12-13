package db.repository.impl;

import db.repository.MessageRepository;
import db.repository.RoomRepository;
import db.repository.UserRepository;
import entities.MessageEntity;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.*;

import java.sql.Timestamp;
import java.util.Date;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageRepositoryImplTest {
    static RoomRepository roomRepository = RepositoryFactory.getRoomRepository();
    static UserRepository userRepository = RepositoryFactory.getUserRepository();
    MessageRepository repository = RepositoryFactory.getMessageRepository();
    static MessageEntity testMessage = new MessageEntity();

    @BeforeAll
    static void initTestUserEntity() {
        testMessage.setToRoom(roomRepository.findById(1));
        testMessage.setDateVal(new Timestamp(new Date().getTime()));
        testMessage.setFromUser(userRepository.findById(1));
        testMessage.setValue("Test message for JUnit");
    }

    @Test
    @Order(1)
    void save() {
        Assertions.assertDoesNotThrow(() -> repository.save(testMessage));
    }

    @Test
    @Order(2)
    void saveWithNullValues() {
        MessageEntity message = new MessageEntity();
        message.setValue(null);
        message.setFromUser(null);
        message.setDateVal(null);
        message.setToRoom(null);

        Assertions.assertThrows(PropertyValueException.class, () -> repository.save(message));
    }

    @Test
    @Order(3)
    void findById() {
        MessageEntity actual = repository.findById(testMessage.getId());

        Assertions.assertEquals(testMessage, actual);
    }

    @Test
    @Order(4)
    void delete() {
        MessageEntity message = repository.findById(testMessage.getId());

        Assertions.assertDoesNotThrow(() -> repository.delete(message));
    }
}