/**
 * projecttask.Struts Feb 18, 2010
 */
package org.iita.security;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.iita.util.PagedResult;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.afterinvocation.AfterInvocationProvider;
import org.springframework.security.vote.AccessDecisionVoter;

/**
 * <p>
 * DataLoadCheck is a Spring framework security voter and after invocation provider. This code will check methods for <code>@Secured</code> annotation with role
 * <b>BF_USERACCESS</b> and classes that implement {@link UserAccess} interface are subject to row-level access check.
 * </p>
 * 
 * <pre>
 * public class OrderServiceImpl implements OrderService {
 * 	&#064;Secured( { &quot;ROLE_ADMIN&quot;, &lt;b&gt;&quot;BF_USERACCESS&quot;&lt;/b&gt; } )
 * 	public Order load(Long id) {
 * 		// ...
 * 	}
 * }
 * </pre>
 * <p>
 * Note: Including <b>ROLE_ADMIN</b> in list of roles will instantly grant access to method and the resulting object to users with that authority.
 * </p>
 * <p>
 * If user does not have ROLE_ADMIN, this access decision manager will grant access to object if row-level access check returns <code>true</code>.
 * </p>
 * 
 * <pre>
 * public class Order implements UserAccess {
 * 	// Return true if user has access to this object (e.g. by checking if user is owner of the order)
 * 	&#064;Override
 * 	public boolean hasAccess(User user) {
 * 		&lt;b&gt;// grant access to Order if user is owner&lt;/b&gt;
 * 		if (this.owner.getId().equals(user.getId())
 * 			return true;
 * 		else
 * 			return false;
 * 	}
 * }
 * </pre>
 * <p>
 * As the method is annotated with <code>@Secured</code> and specifies <b>BF_USERACCESS</b> this voter will grant access to the method and method will be
 * invoked. The object returned from the method call will be checked by DataLoadCheck by calling {@link UserAccess#hasAccess(org.iita.security.model.User)}
 * method on the object.
 * </p>
 * 
 * @author mobreza
 */
public class DataLoadCheck implements AccessDecisionVoter, AfterInvocationProvider {
	private Log LOG = LogFactory.getLog(DataLoadCheck.class);

	/**
	 * Vote to grant user access to @Secure method call.
	 * 
	 * @see org.springframework.security.vote.AccessDecisionVoter#vote(org.springframework.security.Authentication, java.lang.Object,
	 *      org.springframework.security.ConfigAttributeDefinition)
	 */
	@Override
	public int vote(Authentication authentication, Object paramObject, ConfigAttributeDefinition paramConfigAttributeDefinition) {
		LOG.debug("vote: " + authentication + " paramObject=" + paramObject);
		LOG.debug("vote: config=" + paramConfigAttributeDefinition);
		int result = ACCESS_ABSTAIN;
		
		@SuppressWarnings("rawtypes")
		Iterator iter = paramConfigAttributeDefinition.getConfigAttributes().iterator();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			String attr = attribute.getAttribute().toUpperCase();
			if (attr.equalsIgnoreCase("BF_USERACCESS")) {
				LOG.debug("Granting access to method because of: " + attribute);
				return ACCESS_GRANTED;
			}
			if (attr.equalsIgnoreCase("BF_USERWRITE")) {
				LOG.debug("Need to check object passed in if it is writable.");
				if (paramObject instanceof ReflectiveMethodInvocation) {
					LOG.debug("Got RMI");
					ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) paramObject;
					Object[] callArguments = rmi.getArguments();
					for (Object callArgument : callArguments) {
						LOG.debug("Checking argument: " + callArgument);
						if (callArgument instanceof UserWriteAccess) {
							LOG.debug("Is instance of UserWriteAccess");
							UserWriteAccess userWriteAccess = (UserWriteAccess) callArgument;
							if (userWriteAccess.hasWriteAccess(Authorize.getUser(authentication))) {
								LOG.debug("User '" + authentication.getName() + "' has write access to object: " + callArgument);
								result = ACCESS_GRANTED;
							} else {
								LOG.info("User '" + authentication.getName() + "' has no access to object: " + callArgument);
								result = ACCESS_DENIED;
							}
						} else if (callArgument instanceof UserAccess) {
							LOG.debug("Is instance of UserAccess");
							UserAccess userAccess = (UserAccess) callArgument;
							if (userAccess.hasAccess(Authorize.getUser(authentication))) {
								LOG.debug("User '" + authentication.getName() + "' has access to object: " + callArgument);
								result = ACCESS_GRANTED;
							} else {
								LOG.info("User '" + authentication.getName() + "' has no access to object: " + callArgument);
								result = ACCESS_DENIED;
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Only support "BF_" (as in "Business Function" attributes)
	 * 
	 * @see org.springframework.security.afterinvocation.AfterInvocationProvider#supports(org.springframework.security.ConfigAttribute)
	 */
	@Override
	public boolean supports(ConfigAttribute attribute) {
		String attr = attribute.getAttribute().toUpperCase();
		return attr.equalsIgnoreCase("BF_USERACCESS") || attr.equalsIgnoreCase("BF_USERWRITE");
	}

	/**
	 * @see org.springframework.security.afterinvocation.AfterInvocationProvider#supports(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		LOG.info("supports: clazz=" + clazz);
		if (clazz == org.aopalliance.intercept.MethodInvocation.class)
			return true;

		return false;
	}

	/**
	 * Used to decide whether the method return object should be accessible to user.
	 * 
	 * @see org.springframework.security.afterinvocation.AfterInvocationProvider#decide(org.springframework.security.Authentication, java.lang.Object,
	 *      org.springframework.security.ConfigAttributeDefinition, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object decide(Authentication authentication, Object methodInvocation, ConfigAttributeDefinition configAttributeDefinition, Object returnedObject)
			throws AccessDeniedException {
		if (returnedObject == null) {
			LOG.debug("Not deciding on null object");
			return returnedObject;
		}

		LOG.debug("decide: auth=" + authentication.getClass() + ": " + authentication);
		LOG.debug("decide: method=" + methodInvocation);
		LOG.debug("decide: paramObject=" + returnedObject.getClass() + ": " + returnedObject);
		GrantedAuthority[] authorities = authentication.getAuthorities();

		Iterator iter = configAttributeDefinition.getConfigAttributes().iterator();
		while (iter.hasNext()) {
			ConfigAttribute attribute = (ConfigAttribute) iter.next();
			if (attribute.getAttribute().startsWith("ROLE_")) {
				for (int i = 0; i < authorities.length; ++i) {
					if (attribute.getAttribute().equals(authorities[i].getAuthority())) {
						return returnedObject;
					}
				}
			}

			if (supports(attribute)) {
				LOG.debug("Supported: " + attribute);
				// check if has userAcccess
				if (returnedObject instanceof UserAccess) {
					UserAccess userAccess = (UserAccess) returnedObject;
					if (userAccess.hasAccess(Authorize.getUser(authentication)))
						return returnedObject;
					else {
						throw new AccessDeniedException("You do not have permission to access " + returnedObject);
					}
				} else if (returnedObject instanceof PagedResult<?>) {
					// check what we are returning
					PagedResult<?> paged;
					try {
						paged = (PagedResult<?>) ((PagedResult<?>) returnedObject).clone();
						Collection list = new FilteredCollection(paged.getResults()).getFilteredCollection(this, authentication, methodInvocation,
								configAttributeDefinition);
						paged.setResults((List<?>) list);
						return paged;
					} catch (CloneNotSupportedException e) {
						LOG.error(e.getMessage(), e);
					}
				} else if (returnedObject instanceof Collection<?>) {
					Collection<?> col = (Collection<?>) returnedObject;
					return new FilteredCollection(col).getFilteredCollection(this, authentication, methodInvocation, configAttributeDefinition);
				} else {
					LOG.debug("Giving fuck-all about " + returnedObject.getClass());
				}
			}
		}

		return returnedObject;
	}
}
