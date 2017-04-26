package org.iita.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.ConfigAttribute;
import org.springframework.security.ConfigAttributeDefinition;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.InsufficientAuthenticationException;
import org.springframework.security.vote.RoleVoter;

/**
 * Access decision manager checks for required user roles on methods before invoking them. The
 * method needs to be annotated with @Secured annotation, listing allowed roles:
 * 
 * <pre>@Secured("ROLE_UNITHEAD, ROLE_PROJECTMANAGER")
 * public void .....() {</pre>
 * 
 * <p>This implementation is now deprecated in favor of Spring framework's {@link RoleVoter}.</p>
 * @author mobreza
 * 
 */
@Deprecated
public class AccessDecisionManager implements org.springframework.security.AccessDecisionManager {
	private Log LOG = LogFactory.getLog(AccessDecisionManager.class);

	@Override
	public void decide(Authentication authentication, Object object, ConfigAttributeDefinition config) throws AccessDeniedException,
			InsufficientAuthenticationException {
		LOG.debug("decide: " + authentication + " obj=" + object + " conf=" + config);
		if (object instanceof ReflectiveMethodInvocation) {
			@SuppressWarnings("unused")
			ReflectiveMethodInvocation rmi = (ReflectiveMethodInvocation) object;

			isMethodAccessGranted(authentication, config);

			// User user = (User) authentication.getPrincipal();
			//
			// boolean isMember = false;
			// Object[] args = rmi.getArguments();
			// Unit unit = null;
			// Project project = null;
			// Task task = null;
			//
			// for (Object arg : args) {
			// if (arg instanceof Unit) {
			// unit = (Unit) arg;
			// } else if (arg instanceof Project) {
			// project = (Project) arg;
			// unit = project.getUnit();
			// } else if (arg instanceof Task) {
			// task = (Task) arg;
			// project = task.getProject();
			// if (project != null)
			// unit = project.getUnit();
			// break;
			// }
			// }
			//
			// if (task != null && task.isMember(user)) {
			// isMember = true;
			// }
			// if (project != null && project.hasMember(user)) {
			// isMember = true;
			// }
			// if (unit != null && unit.hasMember(user)) {
			// isMember = true;
			// }
			//
			// if (!isMember)
			// throw new AccessDeniedException("Access denied for " + authentication.getName() + ". You are not a member.");
		}
	}

	public static void isMethodAccessGranted(Authentication authentication, ConfigAttributeDefinition config) {
		boolean hasRole = false;

		for (Object configAttribute : config.getConfigAttributes()) {
			String[] requiredRoles = configAttribute.toString().split(",\\s*");
			for (String requiredRole : requiredRoles) {
				if (requiredRole == null || requiredRole.length() == 0)
					continue;
				for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
					if (grantedAuthority.getAuthority().equals(requiredRole)) {
						hasRole = true;
					}
				}
			}
		}

		if (!hasRole)
			throw new AccessDeniedException("Access denied for " + authentication.getName() + ". Insufficient privileges.");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		LOG.info("supports: attr=" + attribute);
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean supports(Class clazz) {
		LOG.info("supports: clazz=" + clazz);
		if (clazz == org.aopalliance.intercept.MethodInvocation.class)
			return true;

		return false;
	}

}
