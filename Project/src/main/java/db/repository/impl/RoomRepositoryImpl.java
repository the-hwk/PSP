package db.repository.impl;

import db.repository.RoomRepository;
import entities.RoomEntity;
import entities.UserEntity;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.Query;

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

    @Override
    public void save(RoomEntity room) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            session.save(room);

            RepositoryFactory.getUserRepository().save(session, room);

            session.getTransaction().commit();
        }
    }
}
