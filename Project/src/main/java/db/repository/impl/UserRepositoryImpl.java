package db.repository.impl;

import db.repository.UserRepository;
import entities.UserEntity;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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

    @Override
    public UserEntity findByEmail(String email) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<UserEntity> cq = cb.createQuery(UserEntity.class);

            Root<UserEntity> rootEntry = cq.from(UserEntity.class);

            CriteriaQuery<UserEntity> all = cq.select(rootEntry).where(cb.equal(rootEntry.get("email"), email));

            TypedQuery<UserEntity> allQuery = session.createQuery(all);
            return allQuery.getSingleResult();
        }
    }
}
