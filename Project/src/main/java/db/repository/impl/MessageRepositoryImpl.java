package db.repository.impl;

import db.repository.MessageRepository;
import entities.MessageEntity;

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
}
