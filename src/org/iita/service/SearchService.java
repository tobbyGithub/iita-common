package org.iita.service;

import org.iita.util.PagedResult;

/**
 * Search service searches for <em>searchString</em> terms in instances of <code>clazz</code> only in <code>fields</code> list.
 * 
 * @author mobreza
 * 
 * @param <T>
 */
public interface SearchService<T> {

	/**
	 * Search <code>fields</code> in <code>clazz</code> type for terms listed in <em>searchQuery</em>.
	 * 
	 * Example below will search <code>User</code> class for occurences of "obreza" in listed fields. Returns matches from 0 to 50.
	 * 
	 * <pre>
	 * service.search("obreza", org.iita.security.model.User.clazz, new String[] { "displayName", "username" }, 0, 50);</pre>
	 * 
	 * 
	 * @see PagedResult
	 * 
	 * @param searchQuery terms to search
	 * @param clazz class to search
	 * @param fields array of fields to be inspected
	 * @param startAt first record returned
	 * @param maxResults max number of records returned
	 * @return PagedResult with <code>clazz</code> instances.
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	PagedResult<? extends T> search(String searchQuery, Class clazz, String[] fields, int startAt, int maxResults) throws SearchException;

	/**
	 * @param queryString
	 * @param fields
	 * @param startAt
	 * @param maxResults
	 * @return
	 * @throws SearchException 
	 */
	PagedResult<?> search(String queryString, String[] fields, int startAt, int maxResults) throws SearchException;
}
