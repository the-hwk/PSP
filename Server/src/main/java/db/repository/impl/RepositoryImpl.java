package db.repository.impl;

import db.repository.Repository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

class RepositoryImpl<T> implements Repository<T> {
    private final Class<T> clazz;

    public RepositoryImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T findById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(clazz, id);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(clazz);

            Root<T> rootEntry = cq.from(clazz);

            CriteriaQuery<T> all = cq.select(rootEntry);

            TypedQuery<T> allQuery = session.createQuery(all);
            return allQuery.getResultList();
        }
    }

    @Override
    public void save(T obj) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(obj);
            tx.commit();
        }
    }

    @Override
    public void update(T obj) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(obj);
            tx.commit();
        }
    }

    @Override
    public void delete(T obj) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(obj);
            tx.commit();
        }
    }
}
