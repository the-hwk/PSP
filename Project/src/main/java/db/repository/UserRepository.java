package db.repository;

import entities.UserEntity;

public interface UserRepository extends Repository<UserEntity> {
    UserEntity findByEmail(String email);
}
