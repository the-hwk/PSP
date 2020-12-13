package db.repository.impl;

import db.repository.PasswordRepository;
import db.repository.UserRepository;
import entities.PasswordEntity;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PasswordRepositoryImplTest {
    static UserRepository userRepository = RepositoryFactory.getUserRepository();
    PasswordRepository repository = RepositoryFactory.getPasswordRepository();
    static PasswordEntity testPass = new PasswordEntity();

    @BeforeAll
    static void initTestUserEntity() {
        testPass.setUser(userRepository.findById(1));
        testPass.setValue("pass".getBytes());
    }

    @Test
    @Order(1)
    void save() {
        Assertions.assertDoesNotThrow(() -> repository.save(testPass));
    }

    @Test
    @Order(2)
    void findByUser() {
        PasswordEntity actual = repository.findByUser(userRepository.findById(1));

        Assertions.assertEquals(testPass, actual);
    }

    @Test
    @Order(3)
    void update() {
        PasswordEntity pass = repository.findById(testPass.getId());
        testPass.setValue("changed_pass".getBytes());

        Assertions.assertDoesNotThrow(() -> repository.update(pass));
    }

    @Test
    @Order(4)
    void delete() {
        PasswordEntity pass = repository.findById(testPass.getId());

        Assertions.assertDoesNotThrow(() -> repository.delete(pass));
    }
}