package utils;

import entities.MessageEntity;
import entities.PasswordEntity;
import entities.RoomEntity;
import entities.UserEntity;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;

    private static final Logger logger = Logger.getLogger(HibernateSessionFactoryUtil.class);

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(MessageEntity.class);
                configuration.addAnnotatedClass(PasswordEntity.class);
                configuration.addAnnotatedClass(RoomEntity.class);
                configuration.addAnnotatedClass(UserEntity.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return sessionFactory;
    }
}
