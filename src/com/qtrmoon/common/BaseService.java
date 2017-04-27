package com.qtrmoon.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 * 
 * <p>
 * <a href="BaseManager.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseService {
	protected final Log log = LogFactory.getLog(getClass());

	protected BaseDAO baseDao = null;

	public BaseDAO getDao() {
		return baseDao;
	}

	public void setDao(BaseDAO dao) {
		this.baseDao = dao;
	}

	/**
	 * @see org.appfuse.service.Service#getObject(java.lang.Class,
	 *      java.io.Serializable)
	 */
	public Object getObject(Class clazz, Serializable id) {
		return baseDao.getObject(clazz, id);
	}

	/**
	 * @see org.appfuse.service.Service#getObjects(java.lang.Class)
	 */
	public List getObjects(Class clazz) {
		return baseDao.getObjects(clazz);
	}

	/** ������ɾ�� */
	public void removeObject(Class clazz, Serializable id) {
		baseDao.removeObject(clazz, id);
	}
	/** ɾ������ */
	public void removeObject(Object obj) {
		baseDao.removeObject(obj);
	}
	/** ɾ������ */
	public void removeAll(Collection<Object> obj) {
		baseDao.removeAll(obj);
	}

	/**
	 * @see org.appfuse.service.Service#saveObject(java.lang.Object)
	 */
	public void saveObject(Object o) {
		baseDao.saveObject(o);
	}
}
