/**
 * iita-common Aug 9, 2010
 */
package org.iita.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.afterinvocation.AfterInvocationProvider;

/**
 * <p>
 * Filtered collection is a utility class used by {@link DataLoadCheck} to return a list of objects filtered through {@link UserAccess} and use of
 * <b>BF_USERACCESS</b> secured annotation.
 * </p>
 * 
 * @author mobreza
 */
public class FilteredCollection<T> {
	private Log LOG = LogFactory.getLog(FilteredCollection.class);

	private Collection<T> source;

	public FilteredCollection(Collection<T> source) {
		this.source = source;
	}

	/**
	 * <p>
	 * Filter source collection by checking each object in collection using provided {@link AfterInvocationProvider} for user access to individual objects.
	 * </p>
	 * <p>
	 * Object where access is denied are replaced with <code>null</code> reference.
	 * </p>
	 * 
	 * @param provider
	 * @param paramAuthentication
	 * @param methodInvo
	 * @param config
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<T> getFilteredCollection(AfterInvocationProvider provider, Authentication paramAuthentication, Object methodInvo,
			ConfigAttributeDefinition config) {
		if (this.source == null || this.source.size() == 0)
			return this.source;
		Iterator<T> it = this.source.iterator();
		Collection<T> filt = new ArrayList<T>();
		while (it.hasNext()) {
			T obj = it.next();
			Object decision = null;
			try {
				decision = provider.decide(paramAuthentication, methodInvo, config, obj);
				filt.add((T) decision);
			} catch (AccessDeniedException e) {
				LOG.info("No access to " + obj);
			}
		}
		return filt;
	}
}
