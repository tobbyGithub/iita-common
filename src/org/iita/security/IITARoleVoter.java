/**
 * iita-common Aug 9, 2010
 */
package org.iita.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.afterinvocation.AfterInvocationProvider;
import org.springframework.security.vote.RoleVoter;

/**
 * Unused class, used to log calls to RoleVoter.
 * 
 * @author mobreza
 */
public class IITARoleVoter extends RoleVoter implements AfterInvocationProvider {
	private Log LOG = LogFactory.getLog(IITARoleVoter.class);

	/**
	 * @see org.springframework.security.afterinvocation.AfterInvocationProvider#decide(org.springframework.security.Authentication, java.lang.Object,
	 *      org.springframework.security.ConfigAttributeDefinition, java.lang.Object)
	 */
	@Override
	public Object decide(Authentication paramAuthentication, Object paramObject1, ConfigAttributeDefinition paramConfigAttributeDefinition, Object paramObject2)
			throws AccessDeniedException {
		LOG.info("decide");
		return paramObject2;
	}

}
