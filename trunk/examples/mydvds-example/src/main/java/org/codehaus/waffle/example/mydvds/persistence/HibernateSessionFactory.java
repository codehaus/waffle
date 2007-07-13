package org.codehaus.waffle.example.mydvds.persistence;

import org.codehaus.waffle.Startable;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateSessionFactory implements Startable {

	private SessionFactory sessionFactory;

	private AnnotationConfiguration configuration;
	
	public Session getSession() {
		return sessionFactory.openSession();
	}

    public void start() {
        try {
            configuration = new AnnotationConfiguration();
            configuration.configure();
            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
        
    }

    public void stop() {
       sessionFactory.close();
    }

}