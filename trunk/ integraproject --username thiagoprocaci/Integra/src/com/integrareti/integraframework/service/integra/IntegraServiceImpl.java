package com.integrareti.integraframework.service.integra;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.integrareti.integraframework.business.Identifiable;
import com.integrareti.integraframework.dao.integra.GenericDao;
import com.integrareti.integraframework.dao.integra.GenericDaoHibernate;

/**
 * This class offers the basics services to access integra database
 * 
 * @author Thiago Baesso Procaci
 * 
 */
@Transactional(rollbackFor = { AppsForYourDomainException.class,
		AuthenticationException.class, ServiceException.class,
		IOException.class })
public class IntegraServiceImpl<T extends Identifiable<ID>, ID extends Serializable>
		implements IntegraServiceInterface<T, ID> {

	private GenericDao<T, ID> genericDao;

	/**
	 * Creates a new IntegraServiceImpl
	 * 
	 * @param genericDao
	 */
	public IntegraServiceImpl(GenericDao<T, ID> genericDao) {
		this.genericDao = genericDao;
	}

	/**
	 * 
	 * @return Returns genericDao
	 */
	public GenericDao<T, ID> getGenericDao() {
		return genericDao;
	}

	/**
	 * Sets genericDao
	 * 
	 * @param genericDao
	 */
	public void setGenericDao(GenericDao<T, ID> genericDao) {
		this.genericDao = genericDao;
	}

	/**
	 * Deletes an object
	 * 
	 * @param object
	 * @return deleted object
	 * @throws Exception
	 */
	@Override
	public void delete(T object) throws Exception {
		genericDao.delete(object);
	}

	/**
	 * @return Returns all
	 * @param class
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> getAll() throws Exception {
		return genericDao.getAll();
	}

	/**
	 * @return Returns an object by id
	 * 
	 * @param class
	 * @param clazz
	 * @param objPK
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public T getById(ID objId) throws Exception {
		return genericDao.getById(objId);
	}

	/**
	 * Saves an object
	 * 
	 * @param object
	 * @throws Exception
	 */
	@Override
	public void save(T object) throws Exception {
		genericDao.save(object);
	}

	/**
	 * @return Returns all by domainName
	 * @param class
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> getAllByDomainName(String domainName) throws Exception {
		return genericDao.getAllByDomainName(domainName);
	}

	/**
	 * Reattach an object to Hibernate Session
	 * 
	 * @param o
	 */
	@Override
	public void reattach(Object o) {
		((GenericDaoHibernate<T, ID>) genericDao).getHibernateTemplate()
				.update(o);
	}

	/**
	 * Initialize an object - hibernate
	 * 
	 * @param o
	 */
	@Override
	public void initialize(Object o) {
		((GenericDaoHibernate<T, ID>) genericDao).getHibernateTemplate()
				.initialize(o);
	}

	/**
	 * close hibernate session
	 */
	@Override
	public void closeSession() {
		genericDao.closeSession();		
	}

	/**
	 * Open hibernate session
	 */
	@Override
	public void openSession() {
		genericDao.openSession();		
	}

}
