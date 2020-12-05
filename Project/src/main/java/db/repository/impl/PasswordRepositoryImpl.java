package db.repository.impl;

import db.repository.PasswordRepository;
import entities.PasswordEntity;

class PasswordRepositoryImpl extends RepositoryImpl<PasswordEntity> implements PasswordRepository {
    public static class SingletonHolder {
        public static final PasswordRepository HOLDER_INSTANCE = new PasswordRepositoryImpl(PasswordEntity.class);
    }

    public static PasswordRepository getInstance() {
        return PasswordRepositoryImpl.SingletonHolder.HOLDER_INSTANCE;
    }

    private PasswordRepositoryImpl(Class<PasswordEntity> clazz) {
        super(clazz);
    }
}
