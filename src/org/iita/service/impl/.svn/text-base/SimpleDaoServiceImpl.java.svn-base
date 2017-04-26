/**
 * travelauth.Struts May 12, 2009
 */
package org.iita.service.impl;

import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.service.SimpleDaoService;
import org.iita.util.PagedResult;
import org.iita.util.StringUtil;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author mobreza
 * 
 */
public abstract class SimpleDaoServiceImpl<T> implements SimpleDaoService<T> {
	protected static final Log LOG = LogFactory.getLog(SimpleDaoService.class);
	private Class<T> persistentClass;
	protected EntityManager entityManager;

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public SimpleDaoServiceImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		LOG.debug(this.persistentClass);
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	@PersistenceContext
	public abstract void setEntityManager(EntityManager entityManager);

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	/**
	 * @see org.iita.service.SimpleDaoService#add(java.lang.Object)
	 */
	@Override
	@Transactional
	public void add(T entity) {
		this.entityManager.persist(entity);
	}

	/**
	 * @see org.iita.service.SimpleDaoService#add(java.lang.Object)
	 */
	@Override
	@Transactional
	public void addOrMerge(T entity) {
		if (this.entityManager.contains(entity))
			this.entityManager.merge(entity);
		else
			this.entityManager.persist(entity);
	}

	/**
	 * @see org.iita.service.SimpleDaoService#find(java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public T find(Object id) {
		return this.entityManager.find(getPersistentClass(), id);
	}

	@Override
	@Transactional(readOnly = true)
	public org.iita.util.PagedResult<T> list(int startAt, int maxResults) {
		return this.list(startAt, maxResults, null);
	}

	/**
	 * @see org.iita.service.SimpleDaoService#list(int, int)
	 */
	@Override
	@Transactional(readOnly = true)
	public PagedResult<T> list(int startAt, int maxResults, String[] orderBy) {
		PagedResult<T> paged = new PagedResult<T>(startAt, maxResults);

		Query query = this.entityManager.createQuery(getQuery(orderBy)).setFirstResult(startAt);
		// list all if maxresults is negative
		if (maxResults >= 0)
			query.setMaxResults(maxResults);
		paged.setResults(query.getResultList());

		if (paged.getResults() != null && paged.getResults().size() > 0) {
			// find max records
			paged.setTotalHits(((Long) this.entityManager.createQuery("select count (*) " + getQuery(null)).getSingleResult()).intValue());
		}
		return paged;
	}

	/**
	 * Get query string
	 * 
	 * @param orderBy
	 * @return
	 */
	protected String getQuery(String where, String[] orderBy) {
		// skip if where string is zero length
		if (where == null || where.length() == 0)
			return getQuery(orderBy);
		return "from " + getPersistentClass().getName() + " where " + where + StringUtil.SPACE + getOrderBy(orderBy);
	}

	/**
	 * Get query string
	 * 
	 * @param orderBy
	 * @return
	 */
	protected String getQuery(String[] orderBy) {
		return "from " + getPersistentClass().getName() + StringUtil.SPACE + getOrderBy(orderBy);
	}

	/**
	 * Generate order by string
	 * 
	 * @param orderBy
	 * @return
	 */
	private String getOrderBy(String[] orderBy) {
		if (orderBy == null || orderBy.length == 0)
			return org.iita.util.StringUtil.EMPTY;
		StringBuffer sb = new StringBuffer(50);
		for (String order : orderBy) {
			if (order == null || order.length() == 0 || order.trim().length() == 0)
				continue;
			sb.append(sb.length() > 0 ? ", " : " order by ");
			sb.append(order);
		}
		return sb.toString();
	}

	/**
	 * @see org.iita.service.SimpleDaoService#merge(java.lang.Object)
	 */
	@Override
	@Transactional
	public void merge(T entity) {
		this.entityManager.merge(entity);
	}

	/**
	 * @see org.iita.service.SimpleDaoService#refresh(java.lang.Object)
	 */
	@Override
	@Transactional
	public void refresh(T entity) {
		this.entityManager.refresh(entity);
	}

	@Override
	@Transactional
	public void remove(T entity) {
		this.entityManager.remove(entity);
	};
}
