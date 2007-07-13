package org.codehaus.waffle.example.mydvds.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PersistenceManager {

	private Session session;

	private Transaction transaction;

	public PersistenceManager(HibernateSessionFactory factory) {
		this.session = factory.getSession();
	}

	public void close() {
		if (this.session.isOpen()) {
			try {
				if (this.hasTransaction()) {
					this.rollback();
				}
			} catch (HibernateException e) {
                // TODO logging
                e.printStackTrace();
            } finally {
				this.session.close();
			}
		}
	}

	public void beginTransaction() {
		if (this.hasTransaction()) {
			this.rollback();
			throw new IllegalStateException(
					"There is no nested transaction support!");
		}
		this.transaction = this.session.beginTransaction();
	}

	public void flush() {
		this.session.flush();
	}

	public void commit() {
		this.session.flush();
		this.transaction.commit();
		this.transaction = null;
	}

	public void rollback() {
		this.transaction.rollback();
		this.transaction = null;
	}

	public boolean hasTransaction() {
		return this.transaction != null && this.transaction.isActive();
	}

    public UserDao getUserDao() {
		return new UserDao(getSession());
	}

    public DvdDao getDvdDao() {
		return new DvdDao(getSession());
	}

    private Session getSession() {
        return session;
    }

}
