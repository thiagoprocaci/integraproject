package com.integrareti.integraframework.dao.integra;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.integrareti.integraframework.business.Identifiable;

/**
 * This class offers the basics operations to access integra database 
 * @author Thiago
 *
 * @param <E>
 * @param <ID>
 */
public abstract class GenericDaoHibernate<E extends Identifiable<ID>, ID extends Serializable>
		extends HibernateDaoSupport implements GenericDao<E, ID> {
	
	@SuppressWarnings("unchecked")
	private Class entityClass;
	
	private SessionFactory sessionFactory;
	private Session session;
	private ApplicationContext ctx;
	
	/**
	 * 
	 * @return Returns entityClass
	 */
	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		return entityClass;
	}

	/**
	 * Sets entityClass
	 * @param entityClass
	 */
	@SuppressWarnings("unchecked")
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 *  Saves an object
	 * @param row
	 */
	public void save(E row) throws Exception {
		getHibernateTemplate().saveOrUpdate(row);
	}

	/**
	 * 
	 * @return Returns all
	 */
	@SuppressWarnings("unchecked")
	public List<E> getAll() throws Exception {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	/**
	 * Deletes an object
	 * @param row
	 */
	public void delete(E row) throws Exception {
		getHibernateTemplate().delete(row);
	}

	/**
	 * 
	 * @param id
	 * @return Returns a object by id
	 */
	@SuppressWarnings("unchecked")
	public E getById(ID id) throws Exception {
		return (E) (getHibernateTemplate().load(getEntityClass(), id));
	}	
	
	/**
	 * 
	 * @return Returns hibernate session
	 */
	public Session getHibernateSession(){
		return getHibernateTemplate().getSessionFactory().getCurrentSession();
	}

	/**
	 * Open hibernate session
	 */
	public void openSession(){
		// the following is necessary for lazy loading
		sessionFactory = (SessionFactory) getAppContext().getBean(
				"sessionFactory");
		// open and bind the session for this test thread.
		session = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory,
				new SessionHolder(session));
		// setup code here
	}
	
	/**
	 * Closed hibernate session
	 */
	public void closeSession(){
		session.flush();
		session.clear();		
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		SessionFactoryUtils.closeSession(session);
	}
	
	/**
	 * 
	 * @return Returns ApplicationContext
	 */
	private ApplicationContext getAppContext() {
		String path = getClass().getResource("/WebContent/WEB-INF/integra-data.xml").toString();
		System.out.println(path);
		if (ctx == null)
			ctx = new FileSystemXmlApplicationContext(
			"/WebContent/WEB-INF/integra-data.xml");
		return ctx;
	}
	
}
