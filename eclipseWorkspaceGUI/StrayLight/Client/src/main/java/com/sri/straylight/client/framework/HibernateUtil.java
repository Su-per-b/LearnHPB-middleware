package com.sri.straylight.client.framework;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.SessionFactory;
import org.hibernate.ejb.HibernateEntityManagerFactory;

// TODO: Auto-generated Javadoc
/**
 * The only purpose of this class is to provide a SessionFactory.
 *
 * @author Christian Bauer
 */
public class HibernateUtil {

    /** The Constant sessionFactory. */
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

    /**
     * Gets the session factory.
     *
     * @return the session factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
