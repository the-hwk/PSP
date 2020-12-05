package db.repository.impl;

import db.repository.UserRepository;
import entities.UserEntity;

class UserRepositoryImpl extends RepositoryImpl<UserEntity> implements UserRepository {
    public static class SingletonHolder {
        public static final UserRepository HOLDER_INSTANCE = new UserRepositoryImpl(UserEntity.class);
    }

    public static UserRepository getInstance() {
        return UserRepositoryImpl.SingletonHolder.HOLDER_INSTANCE;
    }

    private UserRepositoryImpl(Class<UserEntity> clazz) {
        super(clazz);
    }
}
