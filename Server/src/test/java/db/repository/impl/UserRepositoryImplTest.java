package db.repository.impl;

import db.repository.UserRepository;
import entities.UserEntity;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.*;

import javax.persistence.PersistenceException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserRepositoryImplTest {
    UserRepository repository = RepositoryFactory.getUserRepository();
    static UserEntity testUser = new UserEntity();

    @BeforeAll
    static void initTestUserEntity() {
        testUser.setRoleId(0);
        testUser.setNickname("test_user_0");
        testUser.setEmail("test_0@mail.ru");
    }

    @Test
    @Order(1)
    void saveUnique() {
        Assertions.assertDoesNotThrow(() -> repository.save(testUser));
    }

    @Test
    @Order(2)
    void saveNotUnique() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> repository.save(testUser));
    }

    @Test
    @Order(3)
    void saveWithNullValues() {
        UserEntity user = new UserEntity();
        user.setEmail(null);
        user.setNickname(null);
        user.setRoleId(0);

        Assertions.assertThrows(PropertyValueException.class, () -> repository.save(user));
    }

    @Test
    @Order(4)
    void findById() {
        UserEntity actual = repository.findById(testUser.getId());

        Assertions.assertEquals(testUser, actual);
    }

    @Test
    @Order(5)
    void findByEmail() {
        UserEntity actual = repository.findByEmail("test_0@mail.ru");

        Assertions.assertEquals(testUser, actual);
    }

    @Test
    @Order(6)
    void update() {
        UserEntity user = repository.findById(testUser.getId());
        user.setEmail("updated_email@mail.ru");

        Assertions.assertDoesNotThrow(() -> repository.update(user));
    }

    @Test
    @Order(7)
    void updateWithNullValues() {
        UserEntity user = repository.findById(testUser.getId());
        user.setNickname(null);
        user.setEmail(null);

        Assertions.assertThrows(PersistenceException.class, () -> repository.update(user));
    }

    @Test
    @Order(8)
    void delete() {
        UserEntity user = repository.findById(testUser.getId());

        Assertions.assertDoesNotThrow(() -> repository.delete(user));
    }
}