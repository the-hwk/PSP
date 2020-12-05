package db.repository;

import entities.PasswordEntity;
import entities.UserEntity;

public interface PasswordRepository extends Repository<PasswordEntity> {
    PasswordEntity findByUser(UserEntity user);
}
