package db.repository.impl;

import db.repository.MessageRepository;
import entities.MessageEntity;
import entities.RoomEntity;
import org.hibernate.Session;
import utils.HibernateSessionFactoryUtil;

import javax.persistence.Query;
import java.util.List;

class MessageRepositoryImpl extends RepositoryImpl<MessageEntity> implements MessageRepository {
    public static class SingletonHolder {
        public static final MessageRepository HOLDER_INSTANCE = new MessageRepositoryImpl(MessageEntity.class);
    }

    public static MessageRepository getInstance() {
        return MessageRepositoryImpl.SingletonHolder.HOLDER_INSTANCE;
    }

    private MessageRepositoryImpl(Class<MessageEntity> clazz) {
        super(clazz);
    }

    @Override
    public List<MessageEntity> findForRoom(RoomEntity room) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from MessageEntity where room_id = :id");
            query.setParameter("id", room.getId());
            return query.getResultList();
        }
    }
}
