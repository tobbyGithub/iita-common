/**
 * travelauth.Struts May 12, 2009
 */
package org.iita.service;

import org.iita.util.PagedResult;

/**
 * @author mobreza
 * 
 */
public interface SimpleDaoService<T> {
	PagedResult<T> list(int startAt, int maxResults);

	PagedResult<T> list(int startAt, int maxResults, String[] orderBy);

	T find(Object id);

	void add(T entity);
	
	void addOrMerge(T entity);

	void refresh(T entity);

	void merge(T entity);

	void remove(T entity);
}
