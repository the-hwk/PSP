package db.repository.impl;

import db.repository.RoomRepository;
import entities.RoomEntity;

class RoomRepositoryImpl extends RepositoryImpl<RoomEntity> implements RoomRepository {
    public static class SingletonHolder {
        public static final RoomRepository HOLDER_INSTANCE = new RoomRepositoryImpl(RoomEntity.class);
    }

    public static RoomRepository getInstance() {
        return RoomRepositoryImpl.SingletonHolder.HOLDER_INSTANCE;
    }

    private RoomRepositoryImpl(Class<RoomEntity> clazz) {
        super(clazz);
    }
}
