package swingdemo.util;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The only purpose of this class is to provide a SessionFactory
 *
 * @author Christian Bauer
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory;

    static {
        try {
            // Create the SessionFactory from a JPA configuration (persistence.xml)
            EntityManagerFactory emf =
                    Persistence.createEntityManagerFactory("caveatemptor");
            sessionFactory = ((HibernateEntityManagerFactory)emf).getSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
