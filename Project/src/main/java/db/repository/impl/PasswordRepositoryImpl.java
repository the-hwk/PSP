package db.repository.impl;

import db.repository.PasswordRepository;
import entities.PasswordEntity;
import entities.UserEntity;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.Query;

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

    @Override
    public PasswordEntity findByUser(UserEntity user) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from PasswordEntity where user_id = :id");
            query.setParameter("id", user.getId());
            return (PasswordEntity) query.getSingleResult();
        }
    }
}
